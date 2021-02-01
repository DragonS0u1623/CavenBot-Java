package com.dragons0u1.commands.roles;

import static com.dragons0u1.commands.roles.RoleReactCommand.*;

import java.util.*;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class RoleAddCommand extends ICommand {
	
	public RoleAddCommand() {
		this.name = "add";
		this.arguments = "<channel> <messageID> <emote> <role>";
		this.help = "Adds role reaction to a specified message";
		this.category = new RolesCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands").getAsJsonObject("roles"),
			error = getError(e.getAuthor(), e.getGuild());
		String message = json.get("add_message").getAsString();
		
		if (e.getArgs().isBlank()) {
			String no_args = getError(e.getAuthor(), e.getGuild()).getAsJsonObject("roles").get("no_args").getAsString();
			e.reply(no_args);
			return;
		}
		
		if (!e.getSelfMember().hasPermission(Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_HISTORY)) {
			String missing_permission = error.get("missing_permission").getAsString();
			e.reply(String.format(missing_permission, Permission.MESSAGE_ADD_REACTION.getName() + " and " + Permission.MESSAGE_HISTORY.getName()));
			return;
		}

		String[] args = e.getArgs().split(" ", 4);
		Message m = e.getMessage();
		List<Role> roles = m.getMentionedRoles();
		TextChannel channel = m.getMentionedChannels().get(0);
		channel.retrieveMessageById(args[1]).queue((react) -> {
			if (roles.size() == 1) {
				if (!m.getEmotes().isEmpty())
					react.addReaction(m.getEmotes().get(0)).queue();
				else
					react.addReaction(args[2]).queue();
				Document doc = new Document("guildid", e.getGuild().getId()).append("emoji", (!m.getEmotes().isEmpty() ? m.getEmotes().get(0).getId() : args[2]))
						.append("messageid", react.getId()).append("roleid", roles.get(0).getId()).append("channel", channel.getId()).append("toggle", false);
				reactroles.insertOne(doc);
				e.reply(message);
			}
		});
	}
}
