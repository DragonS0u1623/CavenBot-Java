package com.dragons0u1.commands.moderation;

import static com.dragons0u1.Main.*;
import static com.dragons0u1.utils.LanguageUtils.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class WarnCommand extends Command {

	public WarnCommand() {
		this.name = "warn";
		this.arguments = "<user/userID> reason";
		this.help = "Warns the user with the given reason.";
		this.category = new ModerationCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = JsonParser.parseReader(getLanguageFile(getGuildLanguage(e.getGuild()))).getAsJsonObject()
				.getAsJsonObject("commands").getAsJsonObject("moderation").getAsJsonObject("warn");
		JsonObject error = JsonParser.parseReader(getLanguageFile(getGuildLanguage(e.getGuild()))).getAsJsonObject()
				.getAsJsonObject("commands").getAsJsonObject("errors");
		String title = json.get("title").getAsString(), message = json.get("message").getAsString();
		
		if (!e.getMember().hasPermission(Permission.BAN_MEMBERS) && !e.getMember().hasPermission(Permission.KICK_MEMBERS)) {
			String no_permission = error.get("no_permission").getAsString();
			e.reply(no_permission);
			return;
		}
		
		Message m = e.getMessage();
		String[] args = e.getArgs().split(" ", 2);
		String channelID = admin.find(eq("guildid", e.getGuild().getId())).projection(include("audits")).first()
				.getString("audits");
		TextChannel channel = e.getGuild().getTextChannelById(channelID);
		User user;
		if (m.getMentionedUsers().isEmpty())
			user = e.getJDA().getShardManager().getUserById(args[0]);
		else
			user = m.getMentionedUsers().get(0);

		EmbedBuilder embed = EmbedUtils.embedMessageWithTitle(String.format(title, user.getAsTag()), args[1]).setThumbnail(user.getEffectiveAvatarUrl());
		channel.sendMessage(embed.build()).queue();
		e.reply(String.format(message, user.getAsMention()));
	}
}
