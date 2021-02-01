package com.dragons0u1.commands;

import static com.dragons0u1.utils.LanguageUtils.*;

import java.util.function.*;

import com.dragons0u1.utils.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.entities.*;

public abstract class ICommand extends Command {
	protected String arguments = "";
	protected BiConsumer<CommandEvent, Command> helpBiConsumer = new HelpBiConsumer();
	protected Category category = new Category("No Category");

	@Override
	public String getArguments() {
		return arguments;
	}

	@Override
	public Category getCategory() {
		return category;
	}

	public JsonObject getLanguage(User user, Guild guild) {
		return JsonParser.parseReader(getLanguageFile(getUserOrGuildLanguage(user, guild)))
				.getAsJsonObject();
	}

	public JsonObject getLanguage(Guild guild) {
		return JsonParser.parseReader(getLanguageFile(getGuildLanguage(guild))).getAsJsonObject();
	}

	public JsonObject getError(User user, Guild guild) {
		return JsonParser.parseReader(getLanguageFile(getUserOrGuildLanguage(user, guild)))
				.getAsJsonObject().getAsJsonObject("commands").getAsJsonObject("errors");
	}

	public JsonObject getError(Guild guild) {
		return JsonParser.parseReader(getLanguageFile(getGuildLanguage(guild))).getAsJsonObject()
				.getAsJsonObject("commands").getAsJsonObject("errors");
	}
}