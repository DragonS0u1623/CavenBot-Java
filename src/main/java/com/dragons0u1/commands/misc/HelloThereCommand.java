package com.dragons0u1.commands.misc;

import java.awt.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class HelloThereCommand extends ICommand {

	public HelloThereCommand() {
		this.name = "hello_there";
		this.help = "Sends an embed with a Star Wars gif on it";
		this.category = new MiscCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		String gif = "https://cdn.discordapp.com/attachments/716088303622946846/737578683203584030/Hello_There.gif";
		EmbedBuilder embed = EmbedUtils.embedImageWithTitle("Hello There", gif, gif).setColor(Color.BLACK);
		e.reply(embed.build());
	}
}
