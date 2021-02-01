package com.dragons0u1.commands.moderation;

import static com.dragons0u1.utils.StaticReferences.*;

import java.util.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class ClearCommand extends ICommand {

	public ClearCommand() {
		this.name = "clear";
		this.arguments = "<amount>";
		this.help = "Clears the specified amount of messages from the channel. Default is 20";
		this.category = new ModerationCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject error = getError(e.getAuthor(), e.getGuild());
		if (!e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			String no_permission = error.get("no_permission").getAsString();
			e.reply(no_permission);
			return;
		}
		
		if (!e.getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			String missing_permission = error.get("missing_permission").getAsString();
			e.reply(String.format(missing_permission, Permission.MESSAGE_MANAGE.getName()));
			return;
		}
		
		MessageChannel channel = e.getChannel();
		final List<Message> messages = new ArrayList<>();
		messages.add(e.getMessage());
		int limit = 20;
		if (!e.getArgs().isBlank())
			try {
				limit = Integer.parseInt(e.getArgs());
			} catch (Exception ex) { }
		channel.getHistoryBefore(e.getMessage(), limit).queue(history -> {
			for (Message m : history.getRetrievedHistory())
				if (!m.isPinned())
					messages.add(m);
			channel.purgeMessages(messages);
		});
		logger.info(String.format("Deleted %s messages from %s", messages.size(), channel.getName()));
	}
}
