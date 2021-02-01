package com.dragons0u1.commands.moderation;

import java.time.*;
import java.time.format.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class ServerInfoCommand extends ICommand {

	public ServerInfoCommand() {
		this.name = "info";
		this.help = "Gives relevant info about the server.";
		this.category = new ModerationCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json = getLanguage(e.getGuild()).getAsJsonObject("commands").getAsJsonObject("moderation")
				.getAsJsonObject("server_info");
		JsonObject error = getError(e.getGuild());

		if (!e.getMember().hasPermission(Permission.MANAGE_SERVER)) {
			String no_permission = error.get("no_permission").getAsString();
			e.reply(no_permission);
			return;
		}

		int memberCount = 0, botCount = 0;
		for (Member member : e.getGuild().getMembers()) {
			if (member.getUser().isBot())
				botCount++;
			else
				memberCount++;
		}

		String title = json.get("created_at").getAsString(),
				value = json.getAsJsonObject("members").get("value").getAsString();
		EmbedBuilder embed = EmbedUtils.embedMessageWithTitle(
				String.format("%s: %s", e.getGuild().getName(), e.getGuild().getId()), String.format(title, e.getGuild()
						.getTimeCreated().atZoneSameInstant(ZoneId.of("Z")).format(DateTimeFormatter.ISO_LOCAL_DATE)));
		title = json.getAsJsonObject("members").get("title").getAsString();
		embed.addField(title, String.format(value, e.getGuild().getMemberCount(), memberCount, botCount), false);

		title = json.getAsJsonObject("roles").get("title").getAsString();
		value = json.getAsJsonObject("roles").get("value").getAsString();
		embed.addField(title, String.format(value, e.getGuild().getRoles().size()), true);

		title = json.getAsJsonObject("boosters").get("title").getAsString();
		value = json.getAsJsonObject("boosters").get("value").getAsString();
		embed.addField(title, String.format(value, e.getGuild().getBoostCount(), e.getGuild().getBoostTier()), false)
				.setTimestamp(Instant.now())
				.setAuthor(e.getGuild().getOwner().getEffectiveName(), null,
						e.getGuild().getOwner().getUser().getEffectiveAvatarUrl())
				.setThumbnail(e.getGuild().getIconUrl());

		e.reply(embed.build());
	}
}
