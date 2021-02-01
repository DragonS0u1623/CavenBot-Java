package com.dragons0u1.commands.misc.reminders;

import static com.dragons0u1.commands.misc.reminders.ReminderListCommand.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class ReminderListViewCommand extends ICommand {

	public ReminderListViewCommand() {
		this.name = "view";
		this.help = "Views the list of reminders for the user";
		this.category = new MiscCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands")
				.getAsJsonObject("misc").getAsJsonObject("reminder");
		String title = json.get("view").getAsString();
		User user = e.getAuthor();
		List<Document> docs = new ArrayList<>();
		reminders.find(and(eq("guildid", e.getGuild().getId()), eq("user", user.getId()))).projection(include("number", "reminder"))
				.forEach(d -> docs.add(d));
		if (docs.size() == 0) {
			String no_reminders = json.get("no_reminders").getAsString();
			e.reply(no_reminders);
			return;
		}
		EmbedBuilder embed = EmbedUtils.embedMessageWithTitle(String.format(title, user.getAsTag()), "")
				.setColor(Color.PINK).setThumbnail(user.getEffectiveAvatarUrl());
		for (Document doc : docs)
			embed.addField(doc.getString("number"), doc.getString("reminder"), false);
		e.reply(embed.build());
	}
}
