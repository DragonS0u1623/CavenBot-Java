package com.dragons0u1.commands.moderation;

import static com.dragons0u1.Main.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.*;

public class EditWelcomeCommand extends ICommand {

	public EditWelcomeCommand() {
		this.name = "editwelcome";
		this.arguments = "<message>";
		this.help = "Edits the message of the welcome embeds";
		this.category = new ModerationCategory();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands").getAsJsonObject("moderation")
				.getAsJsonObject("edit_welcome");
		JsonObject error = getError(e.getGuild());
		String message = json.get("message").getAsString();
		if (!e.getMember().hasPermission(Permission.MANAGE_SERVER)) {
			String no_permission = error.get("no_permission").getAsString();
			e.reply(no_permission);
			return;
		}

		String args = e.getArgs().replaceAll("@everyone", "everyone").replaceAll("@here", "here");
		admin.findOneAndUpdate(eq("guildid", e.getGuild().getId()), set("welcome_message", args));
		e.reply(String.format(message, args));
	}
}
