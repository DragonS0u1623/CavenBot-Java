package com.dragons0u1.commands.misc.reminders;

import static com.dragons0u1.commands.misc.reminders.ReminderListCommand.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import java.util.*;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.entities.*;

public class ReminderListAddCommand extends ICommand {
	
	public ReminderListAddCommand() {
		this.name = "add";
		this.arguments = "<Thing you want to be reminded of>";
		this.help = "Adds a reminder to the list of reminders for the user";
		this.category = new MiscCategory();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		String reminder = e.getArgs();
		User user = e.getAuthor();
		int number = 0;
		
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands")
				.getAsJsonObject("misc").getAsJsonObject("reminder");
		String message = json.get("add").getAsString();

		if (e.getArgs().isBlank()) {
			String no_args = json.get("no_args").getAsString();
			e.reply(no_args);
			return;
		}

		List<Document> rem = new ArrayList<>();
		reminders.find(and(eq("guildid", e.getGuild().getId()), eq("user", user.getId()))).projection(include("number")).forEach(d -> rem.add(d));
		number = rem.size();

		Document doc = new Document("guildid", e.getGuild().getId()).append("user", user.getId()).append("reminder", reminder)
				.append("number", String.valueOf(number+1));
		reminders.insertOne(doc);
		e.reply(String.format(message, user.getAsMention()));
	}
}
