package com.dragons0u1.commands.anon;

import static com.dragons0u1.Main.*;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;
import com.mongodb.client.*;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class AnonBlacklistCommand extends ICommand {

	MongoCollection<Document> blacklist = database.getCollection("blacklist");

	public AnonBlacklistCommand() {
		this.name = "blacklist";
		this.arguments = "<channel>";
		this.help = "Blacklists the given channel from the anon commands";
		this.userPermissions = new Permission[] { Permission.MANAGE_SERVER };
		this.category = new AnonCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands")
				.getAsJsonObject("anon").getAsJsonObject("blacklist");
		String message = json.get("message").getAsString();
		
		Message m = e.getMessage();
		TextChannel channel = e.getTextChannel();

		if (!m.getMentionedChannels().isEmpty()) {
			channel = m.getMentionedChannels().get(0);
		} else if (!e.getArgs().isBlank()) {
			String[] args = e.getArgs().split(" ", 2);
			channel = e.getJDA().getShardManager().getTextChannelById(args[0]);
		}

		Document doc = new Document("guildid", e.getGuild().getId()).append("channel", channel.getId());
		blacklist.insertOne(doc);
		e.reply(String.format(message, channel.getAsMention()));
	}
}
