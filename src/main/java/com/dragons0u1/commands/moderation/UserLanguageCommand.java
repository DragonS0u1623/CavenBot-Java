package com.dragons0u1.commands.moderation;

import static com.dragons0u1.utils.LanguageUtils.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

public class UserLanguageCommand extends ICommand {

	public UserLanguageCommand() {
		this.name = "user";
		this.arguments = "<language>";
		this.help = "Sets the language for the user.\nValid languages are:\n" + getLanguages();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("general").getAsJsonObject("language");
		String no_args = json.get("no_args").getAsString();
		if (e.getArgs().isBlank() || !isValidLang(e.getArgs())) {
			e.reply(no_args);
			return;
		}

		Document doc = lang.find(eq("userid", e.getAuthor().getId())).first();
		if (doc == null) {
			doc = new Document("userid", e.getAuthor().getId()).append("language", e.getArgs());
			lang.insertOne(doc);
		}
		else
			lang.findOneAndUpdate(eq("userid", e.getAuthor().getId()), set("language", e.getArgs()));

		e.reply(String.format("%s's language set to %s", e.getAuthor().getAsTag(), e.getArgs()));
	}

}
