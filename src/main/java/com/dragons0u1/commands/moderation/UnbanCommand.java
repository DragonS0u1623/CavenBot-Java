package com.dragons0u1.commands.moderation;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class UnbanCommand extends ICommand {
	
	public UnbanCommand() {
		this.name = "unban";
		this.arguments = "<user/userID> <reason>";
		this.help = "Unbans the user from the server. Uses the user ID.";
		this.category = new ModerationCategory();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getGuild()).getAsJsonObject("commands").getAsJsonObject("moderation")
				.getAsJsonObject("unban");
		JsonObject error = getError(e.getGuild());
		String message = json.get("message").getAsString();
		
		if (e.getArgs().isBlank()) {
			String no_args = error.getAsJsonObject("unban").get("no_args").getAsString();
			e.reply(no_args);
			return;
		}
		
		if (!e.getMember().hasPermission(Permission.BAN_MEMBERS)) {
			String no_permission = error.get("no_permission").getAsString();
			e.reply(no_permission);
			return;
		}
		
		if (!e.getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
			String missing_permission = error.get("missing_permission").getAsString();
			e.reply(String.format(missing_permission, Permission.BAN_MEMBERS.getName()));
			return;
		}
		
		User user;
		String[] args = e.getArgs().split(" ", 2);
		String reason = "";
		
		if (args.length > 1 && args[1] != null)
			reason = args[1];

		if (e.getMessage().getMentionedUsers().isEmpty())
			try {
				user = e.getJDA().getUserById(args[0]);
			} catch (Exception ex) { 
				e.reply("I couldn't find that user. Please make sure the ID is correct or tag the user.");
				return;
			}
		else
			user = e.getMessage().getMentionedUsers().get(0);

		if (e.getAuthor().equals(user)) {
			e.reply("You can't unban yourself.");
			return;
		}

		e.getGuild().unban(user).reason(reason).queue((__) -> e.reply(message));
	}

}
