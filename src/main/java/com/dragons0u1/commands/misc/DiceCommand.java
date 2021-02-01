package com.dragons0u1.commands.misc;

import java.util.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

public class DiceCommand extends ICommand {
	
	public DiceCommand() {
		this.name = "dice";
		this.arguments = "<sides>";
		this.help = "Rolls a dice with the amount of sides given. Default is a normal 6-sided dice";
		this.category = new MiscCategory();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands").getAsJsonObject("misc").getAsJsonObject("dice");
		String message;
		String args = e.getArgs();
		int sides = 6;
		if (!args.isBlank())
			sides = Integer.parseInt(args.split(" ")[0]);
		Random r = new Random();
		int roll = r.nextInt(sides) + 1;
		if (roll == 1) {
			message = json.get("critical_fail").getAsString();
			e.reply(String.format(message, e.getAuthor().getAsTag(), sides, roll));
		} else if (roll == sides && sides == 20) {
			message = json.get("nat_20").getAsString();
			e.reply(String.format(message, e.getAuthor().getAsTag(), sides));
		} else {
			message = json.get("normal").getAsString();
			e.reply(String.format(message, e.getAuthor().getAsTag(), sides, roll));
		}
	}
}
