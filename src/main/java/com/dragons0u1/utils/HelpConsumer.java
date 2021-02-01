package com.dragons0u1.utils;

import java.util.*;
import java.util.function.*;

import com.jagrosh.jdautilities.command.*;
import com.jagrosh.jdautilities.command.Command.*;

public class HelpConsumer implements Consumer<CommandEvent> {
	String format = "`%s` - %s\n", prefix;

	@Override
	public void accept(CommandEvent e) {
		CommandClient client = e.getClient();
		prefix = e.getClient().getPrefix().equalsIgnoreCase("@mention") ? e.getClient().getAltPrefix() : e.getClient().getPrefix();
		String reply = "**CavenBot** commands:\n(For more help with commands do m?<command> help)\n\n";
		List<Category> categories = new ArrayList<>();
		for (Command command : client.getCommands())
			if (!categories.contains(command.getCategory()))
				categories.add(command.getCategory());

		for (Category category : categories) {
			reply += String.format("__%s__:\n", category.getName());
			for (Command command : client.getCommands()) {
				if (command.getCategory().equals(category) && !command.isHidden())
					reply += checkArgs(command);
				if (client.getCommands().indexOf(command) == (client.getCommands().size() - 1))
					reply += "\n";
			}
		}
		reply += String.format("\nFor additional help, contact **%s** or join %s",
				e.getJDA().getShardManager().getUserById(client.getOwnerId()).getAsTag(), client.getServerInvite());
		final String r = reply;
		e.replyInDm(reply, (m) -> e.reply("Help message sent in DMs"), (fail) -> e.reply(r));
	}

	private String checkArgs(Command c) {
		String reply = "";
		if (c.getArguments().isBlank())
			reply += String.format(format, prefix + c.getName(), c.getHelp());
		else
			reply += String.format(format, prefix + c.getName() + " " + c.getArguments(), c.getHelp());
		return reply;
	}
}
