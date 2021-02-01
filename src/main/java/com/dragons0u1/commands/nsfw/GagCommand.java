package com.dragons0u1.commands.nsfw;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.jagrosh.jdautilities.command.*;

public class GagCommand extends ICommand {

	public GagCommand() {
		this.name = "gag";
		this.arguments = "<time> <user>";
		this.help = "Gags the user for the specified amount of time. If no time is specified it's permanent until you remove the role.";
		this.category = new NSFWCategory();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		
	}
}
