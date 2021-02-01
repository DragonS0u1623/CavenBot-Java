package com.dragons0u1.commands.misc;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class SupportServerCommand extends ICommand {
	
	public SupportServerCommand() {
		this.name = "support";
		this.help = "Sends an embed with the invite link to the official support server";
		this.category = new MiscCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands")
				.getAsJsonObject("misc").getAsJsonObject("support_server");
		String message = json.get("message").getAsString();
		String invite = e.getClient().getServerInvite();
		EmbedBuilder embed = EmbedUtils.embedImageWithTitle(message, invite, e.getSelfUser().getEffectiveAvatarUrl());
		e.replyInDm(embed.build());
	}
}
