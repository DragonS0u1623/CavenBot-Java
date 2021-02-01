package com.dragons0u1.commands.destiny;

import com.dragons0u1.commands.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.*;

public class RegisterCommand extends ICommand {

	public RegisterCommand() {
		this.name = "register";
		this.help = "A register command for the Destiny API. Links your destiny manifest ID with your discord account";
		this.cooldown = 20;
	}

	@Override
	protected void execute(CommandEvent e) {
		EmbedBuilder embed = new EmbedBuilder().setTitle("Register with CavenBot", "https://CavenBot-Website.jordanmancini.repl.co");
		
		e.replyInDm(embed.build());
//		try {
//			URL url = new URL(arguments);
//		} catch (IOException ex) {
//			e.replyInDm("Error with the Destiny API or our servers. Couldn't register the user.");
//		}
	}
}
