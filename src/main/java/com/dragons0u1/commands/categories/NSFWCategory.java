package com.dragons0u1.commands.categories;

import com.jagrosh.jdautilities.command.Command.*;

public class NSFWCategory extends Category {
	
	public NSFWCategory() {
		super("NSFW", "Sorry you can only use this command in an NSFW channel", event -> event.getTextChannel().isNSFW());
	}
}
