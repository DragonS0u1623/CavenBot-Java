package com.dragons0u1.commands.misc;

import java.awt.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class GeneralKenobiCommand extends ICommand {

	public GeneralKenobiCommand() {
		this.name = "general_kenobi";
		this.help = "Sends a gif from Star Wars";
		this.category = new MiscCategory();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		String gif = "https://cdn.discordapp.com/attachments/716088303622946846/737578915236937768/general_Grievous.gif";
		EmbedBuilder embed = EmbedUtils.embedImageWithTitle("General Kenobi", gif, gif).setColor(Color.BLACK);
		e.reply(embed.build());
	}
}
