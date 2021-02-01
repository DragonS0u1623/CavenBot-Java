package com.dragons0u1.commands.moderation;

import static com.dragons0u1.Main.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import java.awt.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class KickCommand extends ICommand {

	public KickCommand() {
		this.name = "kick";
		this.arguments = "<user/userID> <reason>";
		this.help = "Kicks the user from the server";
		this.category = new ModerationCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getGuild()).getAsJsonObject("commands").getAsJsonObject("moderation")
				.getAsJsonObject("kick");
		JsonObject error = getError(e.getGuild());
		String message = json.get("message").getAsString();
		
		if (e.getArgs().isBlank()) {
			String no_args = error.getAsJsonObject("kick").get("no_args").getAsString();
			e.reply(no_args);
			return;
		}
		
		if (!e.getMember().hasPermission(Permission.KICK_MEMBERS)) {
			String no_permission = error.get("no_permission").getAsString();
			e.reply(no_permission);
			return;
		}
		
		if (!e.getSelfMember().hasPermission(Permission.KICK_MEMBERS)) {
			String missing_permission = error.get("missing_permission").getAsString();
			e.reply(String.format(missing_permission, Permission.KICK_MEMBERS.getName()));
			return;
		}

		Member kick;
		Message m = e.getMessage();
		String[] args = e.getArgs().split(" ", 2);
		String reason = "";
		
		if (args.length > 1 && args[1] != null)
			reason = args[1];

		String auditChannel = admin.find(eq("guildid", e.getGuild().getId())).projection(include("audits")).first()
				.getString("audits");

		if (!m.getMentionedMembers().isEmpty())
			kick = m.getMentionedMembers().get(0);
		else
			try {
				kick = e.getGuild().getMemberById(args[0]);
			} catch (Exception ex) {
				e.reply("I couldn't find that member. Please make sure the ID is correct or tag the member.");
				return;
			}
		
		if (!e.getSelfMember().canInteract(kick)) {
			e.reply("I can't kick that member. They may be have higher roles than me.");
			return;
		}
		
		if (e.getMember().equals(kick)) {
			e.reply("You can't kick yourself.");
			return;
		}
		
		if (!e.getMember().canInteract(kick)) {
			String cant_interact = error.getAsJsonObject("kick").get("cant_interact").getAsString();
			e.reply(cant_interact);
			return;
		}

		if (kick.getId().equals(e.getSelfMember().getId())) {
			String is_self = json.get("is_self").getAsString();
			e.reply(is_self);
			return;
		}
		
		e.getGuild().kick(kick, reason).queue((__) -> e.reply(message));
		
		String title = json.get("title").getAsString(), r = json.get("reason").getAsString();

		EmbedBuilder embed = EmbedUtils.embedMessageWithTitle(String.format(title, kick.getUser().getAsTag()), 
				String.format(r, reason))
				.setThumbnail(kick.getUser().getEffectiveAvatarUrl())
				.setColor(Color.MAGENTA);

		e.getGuild().getTextChannelById(auditChannel).sendMessage(embed.build()).queue();
	}
}
