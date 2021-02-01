package com.dragons0u1.commands.destiny;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.jagrosh.jdautilities.command.*;

public class Destiny2Command extends ICommand {

	public Destiny2Command() {
		this.name = "destiny";
		this.help = "Parent command for all destiny 2 commands";
		this.children = new Command[] { new DestinyXurCommand(), new TestCommand(), new RegisterCommand() };
		this.category = new DestinyCategory();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		e.reply("Please specify a valid child command. Use `m?destiny help` to see all child commands and how to use them");
	}
}
