package com.dragons0u1.commands.misc;

import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class BotPFPCommand extends Command {

	public BotPFPCommand() {
		this.name = "botpfp";
		this.arguments = "";
		this.hidden = true;
		this.help = "Gives the bot's pfp";
	}

	@Override
	protected void execute(CommandEvent e) {
		EmbedBuilder embed = EmbedUtils.embedImageWithTitle("CavenBot's pfp", e.getSelfUser().getEffectiveAvatarUrl(), e.getSelfUser().getEffectiveAvatarUrl());
		e.reply(embed.build());
	}
}
