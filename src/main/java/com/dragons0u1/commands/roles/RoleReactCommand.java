package com.dragons0u1.commands.roles;

import static com.dragons0u1.Main.*;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;
import com.mongodb.client.*;

public class RoleReactCommand extends ICommand {
	
	static MongoCollection<Document> reactroles = database.getCollection("reactroles");
	
	public RoleReactCommand() {
		this.name = "rolereact";
		this.aliases = new String[] { "rr" };
		this.help = "Parent command for role reactions";
		this.children = new Command[] { new RoleAddCommand(), new RoleRemoveCommand(), new RoleDMSettingCommand(), new RoleToggleSettingCommand() };
		this.category = new RolesCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getError(e.getAuthor(), e.getGuild());
		String no_child = json.get("no_child").getAsString();
		e.reply(String.format(no_child, "rolereact"));
	}
}
