package com.dragons0u1.commands.misc;

import static com.dragons0u1.utils.StaticReferences.*;

import java.awt.*;
import java.io.*;
import java.net.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import io.github.cdimascio.dotenv.*;
import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class DefineCommand extends ICommand {

	public DefineCommand() {
		this.name = "define";
		this.arguments = "<word>";
		this.help = "Gets a definition for the given word and a couple of synonyms.";
		this.category = new MiscCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		Dotenv env = Dotenv.load();
		String[] apikeys = { env.get("DICTIONARY_KEY"), env.get("THESAURUS_KEY") }, syn = new String[5];
		String word = e.getArgs().split(" ")[0], api = "https://dictionaryapi.com/api/v3/references/%s/json/%s?key=%s", def = "";
		try {
			URL url = new URL(String.format(api, "collegiate", word, apikeys[0]));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			int responseCode = con.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_ACCEPTED && responseCode != HttpURLConnection.HTTP_OK) {
				logger.warn(
						String.format("Response returned an error of %d: %s", responseCode, con.getResponseMessage()));
				if (responseCode >= 500) {
					e.reply("Sorry but an unexpected server error occured with the dictionary API. Try again in a bit.");
					return;
				} else {
					e.reply("Sorry. An API error occured while trying to find a definition.");
					return;
				}
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line, response = "";
			while ((line = in.readLine()) != null)
				response += line;
			in.close();
			JsonObject dict = JsonParser.parseString(response).getAsJsonArray().get(0).getAsJsonObject();
			int i = 0;
			for (JsonElement el : dict.getAsJsonArray("shortdef"))
				def += String.format("**%d)** %s\n", ++i, el.getAsString());
			url = new URL(String.format(api, "thesaurus", word, apikeys[1]));
			con = (HttpURLConnection) url.openConnection();
			responseCode = con.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_ACCEPTED && responseCode != HttpURLConnection.HTTP_OK) {
				logger.warn(
						String.format("Response returned an error of %d: %s", responseCode, con.getResponseMessage()));
				if (responseCode >= 500) {
					e.reply("Sorry but an unexpected server error occured with the dictionary API. Try again in a bit.");
					return;
				} else {
					e.reply("Sorry. An API error occured while trying to find the synonyms.");
					return;
				}
			}
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			response = "";
			while ((line = in.readLine()) != null)
				response += line;
			in.close();
			dict = JsonParser.parseString(response).getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject("meta");
			i = 0;
			for (JsonElement el : dict.getAsJsonArray("syns")) {
				if (i == 5)
					break;
				for (JsonElement element : el.getAsJsonArray()) {
					if (i == 5)
						break;
					syn[i++] = element.getAsString();
				}
			}
		} catch (IOException ex) {
			e.reply("Sorry an error occured while looking up the definition and synonyms");
			return;
		}
		String synonyms = "";
		for (int i = 1; i <= 5; i++)
			synonyms += String.format("**%d)** %s\n", i, syn[(i-1)]);
		EmbedBuilder embed = EmbedUtils.embedMessageWithTitle(String.format("Definition of **%s**", word), def)
				.addField("Synonyms", synonyms, false).setColor(Color.BLUE);
		e.reply(embed.build());
	}
}
