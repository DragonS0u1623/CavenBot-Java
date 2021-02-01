package com.dragons0u1.events;

import static com.dragons0u1.utils.StaticReferences.*;
import static com.dragons0u1.Main.*;
import static com.mongodb.client.model.Filters.*;

import java.io.*;
import java.net.*;

import org.bson.*;

import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.hooks.*;

public class GuildJoinLeaveListener extends ListenerAdapter {

	@Override
	public void onGuildJoin(GuildJoinEvent e) {
		Document doc1 = new Document("guildid", e.getGuild().getId()).append("welcome", false).append("audits", false)
				.append("roledm", true).append("joinrole", false),
				doc2 = new Document("guildid", e.getGuild().getId()).append("audits", "0").append("welcome", "0")
						.append("welcome_message", "Welcome to the server").append("voicereadymessage", false)
						.append("language", "en_US");
		serverSettings.insertOne(doc1);
		admin.insertOne(doc2);
		logger.debug(String.format("Documents created for %s: %s", e.getGuild().getName(), e.getGuild().getId()));
		try {
			URL url = new URL(String.format("https://cavenbot--jordanmancini.repl.co/guildcount/%d", e.getJDA().getShardManager().getGuilds().size()));
			url.openConnection();
			logger.debug("Updated the guild count on Bots on Discord");
		} catch (IOException ex) {
			logger.info("Couldn't update the guild count. There may have been a change in the website address, or there was an unexpected error.");
		}
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent e) {
		serverSettings.deleteMany(eq("guildid", e.getGuild().getId()));
		admin.deleteMany(eq("guildid", e.getGuild().getId()));
		logger.debug(String.format("Documents deleted for %s: %s", e.getGuild().getName(), e.getGuild().getId()));
		try {
			URL url = new URL(String.format("https://cavenbot--jordanmancini.repl.co/guildcount/%d", e.getJDA().getShardManager().getGuilds().size()));
			url.openConnection();
			logger.debug("Updated the guild count on Bots on Discord");
		} catch (IOException ex) {
			logger.info("Couldn't update the guild count. There may have been a change in the website address, or there was an unexpected error.");
		}
	}
}
