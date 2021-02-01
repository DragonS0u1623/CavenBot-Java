package com.dragons0u1.commands.anon;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class NoAnonCommand extends ICommand {

	public NoAnonCommand() {
		this.name = "block";
		this.arguments = "<user/userID>";
		this.help = "Gives the No Venting role to the given user";
		this.botPermissions = new Permission[] { Permission.MANAGE_ROLES };
		this.userPermissions = new Permission[] { Permission.MANAGE_SERVER, Permission.MANAGE_ROLES };
		this.category = new AnonCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands")
				.getAsJsonObject("anon").getAsJsonObject("no_anon");
		String message = json.get("message").getAsString();
		
		Guild guild = e.getGuild();
		String[] args = e.getArgs().split(" ", 2);
		if (!guild.getRolesByName("No Venting", true).isEmpty()) {
			Member member;
			Role noVent = guild.getRolesByName("No Venting", true).get(0);
			if (!e.getMessage().getMentionedMembers().isEmpty())
				member = e.getMessage().getMentionedMembers().get(0);
			else
				member = guild.getMemberById(args[0]);
			guild.addRoleToMember(member, noVent).queue();
			e.reply(String.format(message, member.getAsMention()));
		} else {
			guild.createRole().setName("No Venting").queue((role) -> {
				Member member;
				if (!e.getMessage().getMentionedMembers().isEmpty())
					member = e.getMessage().getMentionedMembers().get(0);
				else
					member = guild.getMemberById(args[0]);
				guild.addRoleToMember(member, role).queue();
				e.reply(String.format(message, member.getAsMention()));
			});
		}
	}
}
