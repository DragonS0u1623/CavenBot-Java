package com.dragons0u1.commands.nsfw;

import java.awt.*;
import java.util.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class ToyCommand extends ICommand {
	String[] toys = { 
			"anal beads", "butt plug", "dildo", 
			"hot wax", "strap-on", "cock ring", 
			"bullet vibrator", "wand", 
			"nipple clamps", "fleshlight", "benwa balls" 
	};

	public ToyCommand() {
		this.name = "toy";
		this.help = "Gives you a random toy to play with. Have fun~ :wink:";
		this.category = new NSFWCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands")
				.getAsJsonObject("nsfw").getAsJsonObject("toy");
		String title = json.get("title").getAsString(), description = json.get("description").getAsString();
		Random r = new Random();
		int i = r.nextInt(toys.length);
		String toy = toys[i];
		EmbedBuilder embed = EmbedUtils.embedMessageWithTitle(String.format(title, 
				e.getAuthor().getAsTag()), String.format(description, toy)).setColor(Color.RED);
		e.reply(embed.build());
	}
}
