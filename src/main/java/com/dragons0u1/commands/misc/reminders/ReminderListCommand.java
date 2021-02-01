package com.dragons0u1.commands.misc.reminders;

import static com.dragons0u1.Main.*;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;
import com.mongodb.client.*;

public class ReminderListCommand extends ICommand {
	
	static MongoCollection<Document> reminders = database.getCollection("reminders");
	
	public ReminderListCommand() {
		this.name = "remind";
		this.aliases = new String[] { "reminder", "reminders" };
		this.help = "Parent Command for reminders";
		this.children = new Command[] { new ReminderListAddCommand(), new ReminderListRemoveCommand(), new ReminderListViewCommand() };
		this.category = new MiscCategory();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands").getAsJsonObject("errors");
		String message = json.get("no_child").getAsString();
		e.reply(String.format(message, "remind"));
	}
}
