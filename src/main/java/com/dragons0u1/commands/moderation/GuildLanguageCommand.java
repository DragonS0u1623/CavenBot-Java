package com.dragons0u1.commands.moderation;

import static com.dragons0u1.Main.*;
import static com.dragons0u1.utils.LanguageUtils.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.utils.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.*;

public class GuildLanguageCommand extends ICommand {
	
	public GuildLanguageCommand() {
		this.name = "guild";
		this.arguments = "<language>";
		this.help = "Sets the language for the guild.\nValid languages are:\n" + getLanguages();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getGuild()).getAsJsonObject("general").getAsJsonObject("language");
		JsonObject error = getError(e.getAuthor(), e.getGuild());
		String no_args = json.get("no_args").getAsString();

		if (!e.getMember().hasPermission(Permission.MANAGE_SERVER)) {
			String no_permission = error.get("no_permission").getAsString();
			e.reply(no_permission);
			return;
		}

		if (e.getArgs().isBlank() || !isValidLang(e.getArgs())) {
			e.reply(no_args);
			return;
		}

		serverSettings.findOneAndUpdate(eq("guildid", e.getGuild().getId()), set("language", e.getArgs()));
		json = JsonParser.parseReader(LanguageUtils.getLanguageFile(e.getArgs())).getAsJsonObject().getAsJsonObject("general").getAsJsonObject("language");
		String message = json.get("guild_message").getAsString();
		e.reply(String.format(message, e.getArgs()));
	}

}
