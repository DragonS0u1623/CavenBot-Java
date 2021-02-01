package com.dragons0u1.commands.misc;

import static com.dragons0u1.utils.StaticReferences.*;

import java.awt.*;
import java.io.*;
import java.net.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class MemeCommand extends ICommand {

	public MemeCommand() {
		this.name = "meme";
		this.help = "Sends a random meme. Has a cooldown of 20 seconds";
		this.category = new MiscCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		String title = "", postLink = "", memeurl = "", subreddit = "";
		try {
			URL url = new URL("https://meme-api.herokuapp.com/gimme");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			int responseCode = con.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_ACCEPTED && responseCode != HttpURLConnection.HTTP_OK) {
				logger.warn(
						String.format("Response returned an error of %d: %s", responseCode, con.getResponseMessage()));
				if (responseCode >= 500) {
					e.reply("Sorry but an unexpected server error occured with meme API try again in a bit.");
					return;
				} else {
					e.reply("Sorry. An API error occured while trying to find a meme.");
					return;
				}
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line, response = "";
			while ((line = in.readLine()) != null)
				response += line;
			in.close();
			
			JsonObject json = JsonParser.parseString(response).getAsJsonObject();
			title = json.get("title").getAsString();
			postLink = json.get("postLink").getAsString();
			memeurl = json.get("url").getAsString();
			subreddit = json.get("subreddit").getAsString();

			EmbedBuilder embed = EmbedUtils.embedImageWithTitle(title, postLink, memeurl).setColor(Color.YELLOW)
					.setDescription(String.format("Meme for %s from subreddit [r/%s](https://www.reddit.com/r/%s)",
							e.getAuthor().getAsTag(), subreddit, subreddit));
			e.reply(embed.build());
		} catch (Exception ex) {
			e.reply("Sorry. Something went wrong. I couldn't get a meme for you.");
			ex.printStackTrace();
		}
	}
}
