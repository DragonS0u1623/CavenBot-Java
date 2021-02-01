package com.dragons0u1.commands.anon;

import static com.dragons0u1.Main.*;
import static com.mongodb.client.model.Filters.*;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;
import com.mongodb.client.*;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class RemoveBlacklistCommand extends ICommand {

	MongoCollection<Document> blacklist = database.getCollection("blacklist");
	
	public RemoveBlacklistCommand() {
		this.name = "remove_blacklist";
		this.arguments = "<channel>";
		this.help = "Removes a channel from the anon blacklist.";
		this.userPermissions = new Permission[] { Permission.MANAGE_SERVER };
		this.category = new AnonCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands")
				.getAsJsonObject("anon").getAsJsonObject("remove_blacklist");
		String message = json.get("message").getAsString();
		
		Message m = e.getMessage();
		TextChannel channel = e.getTextChannel();

		if (!m.getMentionedChannels().isEmpty()) {
			channel = m.getMentionedChannels().get(0);
		} else if (!e.getArgs().isBlank()) {
			String[] args = e.getArgs().split(" ", 2);
			channel = e.getJDA().getShardManager().getTextChannelById(args[0]);
		}

		blacklist.deleteMany(and(eq("guildid", e.getGuild().getId()), eq("channel", channel.getId())));
		e.reply(String.format(message, channel.getAsMention()));
	}
}
