package com.dragons0u1.commands.nsfw;

import java.io.*;
import java.net.*;
import java.util.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class PornCommand extends ICommand {

	public PornCommand() {
		this.name = "porn";
		this.arguments = "<tag>";
		this.help = "Gets a video of porn with the given tag(s). No tag is random video.";
		this.category = new NSFWCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		String args = e.getArgs().replaceAll("@everyone", "everyone").replaceAll("@here", "here").replaceAll(" ", "%20"),
				APIUrl = "https://api.redtube.com/?data=redtube.Videos.searchVideos&output=json&page=10%s", gif = "",
				title = "", thumbnail = "";
		try {
			URL url = new URL(String.format(APIUrl, getSearchString(args)));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_ACCEPTED || responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line, response = "";
				while ((line = in.readLine()) != null)
					response += line;
				in.close();
				
				Random r = new Random();
				JsonObject json = JsonParser.parseString(response).getAsJsonObject();
				int count = json.getAsJsonArray("videos").size();
				json = json.getAsJsonArray("videos").get(r.nextInt(count)).getAsJsonObject().getAsJsonObject("video");
				gif = json.get("embed_url").getAsString();
				title = json.get("title").getAsString();
				thumbnail = json.get("default_thumb").getAsString();
			}
		} catch (IOException ex) {
			JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands").getAsJsonObject("nsfw").getAsJsonObject("porn");
			String io = json.get("io").getAsString();
			e.reply(io);
			return;
		} catch (Exception ex) {
			e.reply("Something went wrong. Exiting command");
		}
		EmbedBuilder embed = EmbedUtils.embedImageWithTitle(title, gif, thumbnail);
		e.reply(embed.build());
	}

	private String getSearchString(String args) {
		if (args.isBlank()) {
			return "";
		} else {
			return String.format("&search=%s", args);
		}
	}
}
