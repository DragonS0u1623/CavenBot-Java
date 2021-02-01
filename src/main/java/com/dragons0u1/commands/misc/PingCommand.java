package com.dragons0u1.commands.misc;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.jagrosh.jdautilities.command.*;

public class PingCommand extends ICommand {
	
	public PingCommand() {
		this.name = "ping";
		this.help = "Gets the Gateway ping of the bot.";
		this.guildOnly = false;
		this.category = new MiscCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		e.reply("Pong! " + e.getJDA().getGatewayPing() + "ms");
	}
}
