package com.dragons0u1.commands.roles;

import static com.dragons0u1.commands.roles.RoleReactCommand.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.entities.*;

public class RoleRemoveCommand extends ICommand {

	public RoleRemoveCommand() {
		this.name = "remove";
		this.arguments = "<messageID> <role>";
		this.help = "Removes reaction roles from the specified message";
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands").getAsJsonObject("roles");
		String message = json.get("remove_message").getAsString();
		
		if (e.getArgs().isBlank()) {
			String no_args = getError(e.getAuthor(), e.getGuild()).getAsJsonObject("roles").get("no_args").getAsString();
			e.reply(no_args);
			return;
		}

		String[] args = e.getArgs().split(" ", 2);
		Message m = e.getMessage();
		Role role = m.getMentionedRoles().get(0);
		Document doc = reactroles.find(and(eq("guildid", e.getGuild().getId()), eq("messageid", args[0]), eq("roleid", role.getId())))
				.projection(include("channel", "emoji")).first();
		String channelID = doc.getString("channel");
		TextChannel channel = e.getGuild().getTextChannelById(channelID);
		channel.retrieveMessageById(args[0]).queue((react) -> {
			try {
				Emote emote = e.getGuild().getEmoteById(doc.getString("emoji"));
				react.removeReaction(emote).queue();
			} catch (NumberFormatException ex) {
				react.removeReaction(doc.getString("emoji")).queue();
			}
			reactroles.findOneAndDelete(and(eq("guildid", e.getGuild().getId()), eq("messageid", args[0]), eq("roleid", role.getId())));
			e.reply(message);
		});
	}

}
