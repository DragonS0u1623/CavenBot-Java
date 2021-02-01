package com.dragons0u1.commands.misc;

import java.awt.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class InviteCommand extends ICommand {

	public InviteCommand() {
		this.name = "invite";
		this.help = "Sends the user an embed with an invite link. Please add me to all your favourite servers";
		this.category = new MiscCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("general").getAsJsonObject("invite");
		String title = json.get("title").getAsString();
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setTitle(title,
				"https://discord.com/api/oauth2/authorize?client_id=638446270469373972&permissions=1043590262&scope=bot")
				.setColor(Color.BLUE).setThumbnail(e.getSelfUser().getEffectiveAvatarUrl());
		e.replyInDm(embed.build());
	}
}
