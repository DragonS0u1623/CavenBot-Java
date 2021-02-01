package com.dragons0u1.commands.misc;

import java.awt.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class OmaeCommand extends ICommand {

	public OmaeCommand() {
		this.name = "omae";
		this.help = "Gives an anime joke";
		this.category = new MiscCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		String gif = "https://cdn.discordapp.com/attachments/640674672618373132/647205970442846220/20191121_172359.jpg";
		EmbedBuilder embed = EmbedUtils.embedImageWithTitle("Omae wa mou shindeiru", gif, gif).setColor(Color.ORANGE);
		e.reply(embed.build());
		embed.setTitle("NANI!!!").setColor(Color.RED)
				.setImage("https://cdn.discordapp.com/attachments/640674672618373132/711291888594059354/tenor-4.gif");
		e.reply(embed.build());
	}
}
