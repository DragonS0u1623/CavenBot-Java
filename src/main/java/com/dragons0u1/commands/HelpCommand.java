package com.dragons0u1.commands;

import java.util.*;

import com.jagrosh.jdautilities.command.*;

public class HelpCommand extends ICommand {
	String format = "`%s` - %s\n", prefix;

	public HelpCommand() {
		this.name = "help";
		this.hidden = true;
		this.guildOnly = false;
	}

	@Override
	protected void execute(CommandEvent e) {
		List<CommandClient> clients = new ArrayList<>();
		e.getJDA().getRegisteredListeners().forEach((listener) -> {
			if (listener instanceof CommandClient)
				clients.add((CommandClient) listener);
		});

		String reply = "**CavenBot** commands:\n(For more help with commands do m?<command> help)\n";
		List<Category> categories = new ArrayList<>();

		for (CommandClient client : clients) {
			prefix = client.getPrefix().equalsIgnoreCase("@mention") ? client.getAltPrefix() : client.getPrefix();
			for (Command command : client.getCommands())
				if (!categories.contains(command.getCategory()))
					categories.add(command.getCategory());

			for (Category category : categories) {
				reply += String.format("__%s__:\n", category.getName());
				for (Command command : client.getCommands())
					if (command.getCategory().equals(category) && !command.isHidden())
						reply += checkArgs(command);
			}
			categories.clear();

			//int length = reply.length();
			final String r = reply;
			e.replyInDm(reply, null, (fail) -> e.reply(r));
			clients.remove(client);
		}

		reply = String.format("For additional help, contact **%s** or join %s",
				e.getJDA().getShardManager().getUserById(e.getClient().getOwnerId()).getAsTag(), e.getClient().getServerInvite());
		e.replyInDm(reply);
		e.reply("Help message sent in DMs");
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
