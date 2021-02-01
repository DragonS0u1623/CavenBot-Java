package com.dragons0u1.commands.nsfw;

import java.awt.*;
import java.util.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class PunishmentCommand extends ICommand {
	String[] punishments = { "spanking", "handcuffs", "rope harness", "gag", "edging", "chastity", "ruined orgasm",
			"ball stretcher", "humbler", "nothing" };

	public PunishmentCommand() {
		this.name = "punishme";
		this.aliases = new String[] { "punish", "pm" };
		this.help = "Gives a random punishment. Better be good~";
		this.category = new NSFWCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands")
				.getAsJsonObject("nsfw").getAsJsonObject("punishment");
		String title = json.get("title").getAsString(), description = json.get("description").getAsString();
		Random r = new Random();
		int i = r.nextInt(punishments.length);
		String punishment = punishments[i];
		EmbedBuilder embed = EmbedUtils.embedMessageWithTitle(String.format(title, e.getAuthor().getAsTag()), 
				String.format(description, punishment)).setColor(Color.RED);
		json = json.getAsJsonObject("duration");
		title = json.get("title").getAsString();
		int count;
		switch (punishment) {
		case "spanking":
			description = json.get("spanking").getAsString();
			count = r.nextInt(150) + 1;
			embed.addField(title, String.format(description, count), false);
			break;
		case "edging":
			description = json.get("edges").getAsString();
			count = r.nextInt(10) + 1;
			embed.addField(title, String.format(description, count), false);
			break;
		case "ruined orgasm":
			description = json.get("ruins").getAsString();
			count = r.nextInt(10) + 1;
			embed.addField(title, String.format(description, count), false);
			break;
		case "chastity":
			description = json.get("days").getAsString();
			count = r.nextInt(5) + 1;
			embed.addField(title, String.format(description, count), false);
			break;
		case "nothing":
			title = json.get("punishment").getAsString();
			description = json.get("nothing").getAsString();
			embed.clearFields().addField(title, description, false);
			break;
		default:
			description = json.get("hours").getAsString();
			count = r.nextInt(5) + 1;
			embed.addField("Duration", String.format(description, count), false);
			break;
		}
		e.reply(embed.build());
	}
}
