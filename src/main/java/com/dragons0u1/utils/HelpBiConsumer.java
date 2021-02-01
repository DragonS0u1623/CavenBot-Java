package com.dragons0u1.utils;

import java.util.function.*;

import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.*;

public class HelpBiConsumer implements BiConsumer<CommandEvent, Command> {
	String format = "`%s` - %s\n", format2 = "`%s`\n", prefix;

	@Override
	public void accept(CommandEvent e, Command u) {
		e.reactSuccess();
		
		prefix = e.getClient().getPrefix().equalsIgnoreCase("@mention") ? e.getClient().getAltPrefix()
				: e.getClient().getPrefix();

		String reply = "";
		reply += checkArgs(u);
		reply += checkAliases(u);
		reply += checkCategory(u);
		reply += checkPerms(u);

		if (getChildren(u).length != 0) {
			reply += "\n**Children**\n\n";
			for (Command c : getChildren(u)) {
				reply += checkChildArgs(u, c);
				reply += checkChildAliases(u, c);
			}
		}

		e.replyInDm(reply);
	}

	private Command[] getChildren(Command c) {
		return c.getChildren();
	}

	private String checkArgs(Command c) {
		String reply = "";
		if (c.getArguments().isBlank() || c.getArguments().isEmpty() || c.getArguments() == null)
			reply += String.format(format, prefix + c.getName(), c.getHelp());
		else
			reply += String.format(format, prefix + c.getName() + " " + c.getArguments(), c.getHelp());
		return reply;
	}

	private String checkChildArgs(Command parent, Command child) {
		String reply = "";
		if (child.getArguments().isBlank() || child.getArguments().isEmpty() || child.getArguments() == null)
			reply += String.format(format, prefix + parent.getName() + " " + child.getName(), child.getHelp());
		else
			reply += String.format(format,
					prefix + parent.getName() + " " + child.getName() + " " + child.getArguments(), child.getHelp());
		return reply;
	}

	private String checkPerms(Command c) {
		String reply = "";
		if (c.getUserPermissions().length != 0) {
			reply += "\n**Permissions**\n\n";
			for (Permission p : c.getUserPermissions())
				reply += String.format(format2, p.getName());
		}
		return reply;
	}

	private String checkCategory(Command c) {
		String reply = "\n**Category**\n\n";
		if (c.getCategory() == null)
			reply += String.format(format2, "None");
		else
			reply += String.format(format2, c.getCategory().getName());
		return reply;
	}

	private String checkAliases(Command c) {
		String reply = "";
		if (c.getAliases().length != 0) {
			reply = "\n**Aliases**\n\n";
			for (String alias : c.getAliases())
				reply += String.format(format2, prefix + alias);
		}
		return reply;
	}

	private String checkChildAliases(Command parent, Command child) {
		String reply = "";
		if (child.getAliases().length != 0) {
			reply = "\n**Aliases**\n\n";
			for (String alias : child.getAliases())
				reply += String.format(format2, prefix + parent.getName() + " " + alias);
		}
		return reply;
	}
}
