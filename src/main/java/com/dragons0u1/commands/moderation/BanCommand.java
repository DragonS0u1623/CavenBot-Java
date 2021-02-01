package com.dragons0u1.commands.moderation;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class BanCommand extends ICommand {
	
	public BanCommand() {
		this.name = "ban";
		this.arguments = "<user/userID> <reason>";
		this.help = "Bans the user from the server.";
		this.category = new ModerationCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getGuild()).getAsJsonObject("commands").getAsJsonObject("moderation")
				.getAsJsonObject("ban");
		JsonObject error = getError(e.getGuild());
		String message = json.get("message").getAsString();
		
		if (e.getArgs().isBlank()) {
			String no_args = error.getAsJsonObject("ban").get("no_args").getAsString();
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
		
		Member ban;
		Message m = e.getMessage();
		String[] args = e.getArgs().split(" ", 2);
		String reason = "";
		
		if (args.length > 1 && args[1] != null)
			reason = args[1];

		if (!m.getMentionedMembers().isEmpty())
			ban = m.getMentionedMembers().get(0);
		else
			try {
				ban = e.getGuild().getMemberById(args[0]);
			} catch (Exception ex) {
				e.reply("I couldn't find that member. Please make sure the ID is correct or tag the member.");
				return;
			}
		
		if (!e.getSelfMember().canInteract(ban)) {
			e.reply("I can't ban that member. They may be have higher roles than me.");
			return;
		}
		
		if (e.getMember().equals(ban)) {
			e.reply("You can't ban yourself.");
			return;
		}
		
		if (!e.getMember().canInteract(ban)) {
			String cant_interact = error.getAsJsonObject("ban").get("cant_interact").getAsString();
			e.reply(cant_interact);
			return;
		}
		
		if (ban.getId().equals(e.getSelfMember().getId())) {
			String is_self = json.get("is_self").getAsString();
			e.reply(is_self);
			return;
		}

		e.getGuild().ban(ban.getId(), 7, reason).queue((__) -> e.reply(message));
	}
}
