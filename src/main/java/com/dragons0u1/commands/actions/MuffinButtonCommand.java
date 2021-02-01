package com.dragons0u1.commands.actions;

import java.awt.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class MuffinButtonCommand extends ICommand {

	public MuffinButtonCommand() {
		this.name = "muffin_button";
		this.aliases = new String[] { "mb" };
		this.help = "Sends a DBZA meme of the muffin button";
		this.category = new ActionsCategory();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		String gif = "https://cdn.discordapp.com/attachments/716088303622946846/746103226008600657/muffin_button.gif";
		EmbedBuilder embed = EmbedUtils.embedImageWithTitle("Muffin Button", gif, gif).setColor(Color.black);
		e.reply(embed.build());
	}
}
