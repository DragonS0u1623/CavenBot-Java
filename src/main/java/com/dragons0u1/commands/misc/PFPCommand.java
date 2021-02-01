package com.dragons0u1.commands.misc;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class PFPCommand extends ICommand {

	public PFPCommand() {
		this.name = "pfp";
		this.arguments = "<user>";
		this.help = "Sends an embed with the user's pfp along with a clickable link to it.";
		this.category = new MiscCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		User user = e.getAuthor();
		if (!e.getMessage().getMentionedUsers().isEmpty())
			user = e.getMessage().getMentionedUsers().get(0);
		EmbedBuilder embed = EmbedUtils.embedImageWithTitle(String.format("%s's Profile Pic", user.getAsTag()),
				user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl());
		e.reply(embed.build());
	}
}
