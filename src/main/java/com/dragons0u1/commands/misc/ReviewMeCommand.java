package com.dragons0u1.commands.misc;

import java.awt.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class ReviewMeCommand extends ICommand {

	public ReviewMeCommand() {
		this.name = "review";
		this.help = "Come give me a review on Bots on Discord.";
		this.category = new MiscCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands")
				.getAsJsonObject("misc").getAsJsonObject("review");
		String title = json.get("title").getAsString();
		EmbedBuilder embed = EmbedUtils
				.embedMessageWithTitle(title, "https://bots.ondiscord.xyz/bots/638446270469373972/review")
				.setThumbnail(e.getSelfUser().getEffectiveAvatarUrl()).setColor(new Color(182, 31, 219));
		e.reply(embed.build());
	}
}
