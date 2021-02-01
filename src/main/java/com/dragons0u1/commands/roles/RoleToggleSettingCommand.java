package com.dragons0u1.commands.roles;

import static com.dragons0u1.commands.roles.RoleReactCommand.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import java.util.*;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

public class RoleToggleSettingCommand extends ICommand {
	
	public RoleToggleSettingCommand() {
		this.name = "toggle";
		this.arguments = "<messageid>";
		this.help = "Sets whether roles on this message id toggle on or off";
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands").getAsJsonObject("roles");
		String enable = json.get("toggle_enable").getAsString(), disable = json.get("toggle_disable").getAsString();
		String[] args = e.getArgs().split(" ", 2);
		List<Document> documents = new ArrayList<>();
		reactroles.find(and(eq("guildid", e.getGuild().getId()), eq("messageid", args[0])))
		.projection(include("toggle")).forEach(doc -> documents.add(doc));
		boolean toggle = documents.get(0).getBoolean("toggle");
		int i = 1;
		for (Document doc : documents) {
			doc.put("toggle", !toggle);
			reactroles.replaceOne(and(eq("guildid", e.getGuild().getId()), eq("messageid", args[0]), eq("roleid", doc.getString("roleid"))), doc);
			i++;
		}
		e.reply(String.format((!toggle ? enable : disable), i));
	}
}
