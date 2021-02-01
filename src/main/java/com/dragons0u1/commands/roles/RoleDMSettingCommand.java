package com.dragons0u1.commands.roles;

import static com.dragons0u1.Main.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Updates.*;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

public class RoleDMSettingCommand extends ICommand {
	
	public RoleDMSettingCommand() {
		this.name = "dm";
		this.help = "Toggles the DM settings for role reactions. By default this is enabled.";
	}
	
	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands").getAsJsonObject("roles");
		String enable = json.get("dm_enable").getAsString(), disable = json.get("dm_disable").getAsString();
		Document settings = serverSettings.find(eq("guildid", e.getGuild().getId())).projection(include("roledm")).first();
		boolean dm = settings.getBoolean("roledm");
		serverSettings.findOneAndUpdate(eq("guildid", e.getGuild().getId()), set("roledm", !dm));
		e.reply((!dm ? enable : disable));
	}
}
