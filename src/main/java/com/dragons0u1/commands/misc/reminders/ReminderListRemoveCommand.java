package com.dragons0u1.commands.misc.reminders;

import static com.dragons0u1.commands.misc.reminders.ReminderListCommand.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.entities.*;

public class ReminderListRemoveCommand extends ICommand {
	
	public ReminderListRemoveCommand() {
		this.name = "remove";
		this.arguments = "<number>";
		this.help = "Removes a reminder from the list of reminders for the user";
		this.category = new MiscCategory();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands")
				.getAsJsonObject("misc").getAsJsonObject("reminder");
		String message = json.get("remove").getAsString();
		
		if (e.getArgs().isBlank()) {
			String no_args = json.get("remove_no_args").getAsString();
			e.reply(no_args);
			return;
		}
		User user = e.getAuthor();
		int index = 0;
		try {
			index = Integer.parseInt(e.getArgs().split(" ", 2)[0]);
		} catch (Exception ex) {
			e.reply("Please give a valid number");
		}
		Document doc = reminders.find(and(eq("guildid", e.getGuild().getId()), eq("user", user.getId()))).projection(include("number")).first();
		String reminder = doc.getString("reminder");
		reminders.findOneAndDelete(and(eq("guildid", e.getGuild().getId()), eq("user", user.getId()), eq("number", "" + index)));
		e.reply(String.format(message, reminder, user.getAsMention()));
	}
}
