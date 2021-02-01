package com.dragons0u1.commands.destiny;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import io.github.cdimascio.dotenv.*;

public class DestinyCommand extends ICommand {
	public static String APIKEY = Dotenv.load().get("DESTINY_APIKEY");

	public DestinyCommand() {
		this.name = "destiny1";
		this.help = "Parent command for all destiny 1 (legacy) commands";
		this.children = new Command[] { new Destiny1XurCommand(), new TestCommand() };
		this.category = new DestinyCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject error = getError(e.getAuthor(), e.getGuild());
		String no_child = error.get("no_child").getAsString();
		e.reply(String.format(no_child, "destiny1"));
	}
}
