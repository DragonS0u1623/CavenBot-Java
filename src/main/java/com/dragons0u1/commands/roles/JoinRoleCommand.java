package com.dragons0u1.commands.roles;

import static com.dragons0u1.Main.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;
import com.mongodb.client.*;

import net.dv8tion.jda.api.entities.*;

public class JoinRoleCommand extends ICommand {
	MongoCollection<Document> joinrole = database.getCollection("joinrole");

	public JoinRoleCommand() {
		this.name = "joinrole";
		this.arguments = "<role>";
		this.help = "Sets the role to look add to newly joined members, and enables it. Using this with no role will disable it.";
		this.category = new RolesCategory();
	}
	
	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands").getAsJsonObject("roles");
		
		if (e.getArgs().isBlank() || e.getMessage().mentionsEveryone()) {
			String join_role_disable = json.get("join_role_disable").getAsString();
			serverSettings.findOneAndUpdate(eq("guildid", e.getGuild().getId()), set("joinrole", false));
			joinrole.findOneAndDelete(eq("guildid", e.getGuild().getId()));
			e.reply(join_role_disable);
		} else {
			if (e.getMessage().getMentionedRoles().isEmpty()) {
				String no_join_role = getError(e.getAuthor(), e.getGuild()).getAsJsonObject("roles").get("no_join_role").getAsString();
				e.reply(no_join_role);
				return;
			}

			String join_role_enable = json.get("join_role_enable").getAsString();
			Role role = e.getMessage().getMentionedRoles().get(0);
			if (joinrole.find(eq("guildid", e.getGuild().getId())).first() == null) {
				Document doc = new Document("guildid", e.getGuild().getId()).append("role", role.getId());
				joinrole.insertOne(doc);
			} else
				joinrole.findOneAndUpdate(eq("guildid", e.getGuild().getId()), set("role", role.getId()));
			serverSettings.findOneAndUpdate(eq("guildid", e.getGuild().getId()), set("joinrole", true));
			e.reply(String.format(join_role_enable, role.getAsMention()));
		}
	}
}
