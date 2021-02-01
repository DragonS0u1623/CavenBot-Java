package com.dragons0u1.commands.destiny;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.jagrosh.jdautilities.command.*;

public class DestinyXurCommand extends ICommand {

	public DestinyXurCommand() {
		this.name = "xur";
		this.help = "Gets the current inventory of Xur";
		this.category = new DestinyCategory();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		
	}
}
