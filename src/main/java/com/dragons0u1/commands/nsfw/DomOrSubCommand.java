package com.dragons0u1.commands.nsfw;

import java.util.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.entities.*;

public class DomOrSubCommand extends ICommand {

	public DomOrSubCommand() {
		this.name = "dom";
		this.aliases = new String[] {"sub"};
		this.arguments = "<user>";
		this.help = "Tells you if you are a dom or a sub";
		this.category = new NSFWCategory();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands").getAsJsonObject("nsfw").getAsJsonObject("dom_or_sub");
		String dom = json.get("dom").getAsString(), sub = json.get("sub").getAsString();
		Random r = new Random();
		User user = e.getAuthor();
		if (!e.getMessage().getMentionedUsers().isEmpty())
			user = e.getMessage().getMentionedUsers().get(0);
		e.reply(String.format((r.nextBoolean() ? dom : sub), user.getAsTag()));
	}
}
