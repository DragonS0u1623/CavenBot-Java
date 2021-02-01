package com.dragons0u1.commands.misc;

import java.awt.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class NaniCommand extends ICommand {

	public NaniCommand() {
		this.name = "nani";
		this.help = "Sends an anime joke response";
		this.category = new MiscCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		String gif = "https://cdn.discordapp.com/attachments/640674672618373132/711291888594059354/tenor-4.gif";
		EmbedBuilder embed = EmbedUtils.embedImageWithTitle("NANI!!!", gif, gif).setColor(Color.RED);
		e.reply(embed.build());
	}
}
