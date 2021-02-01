package com.dragons0u1.commands.misc;

import static com.dragons0u1.Main.*;
import static com.dragons0u1.utils.StaticReferences.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import com.dragons0u1.*;
import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.*;
import me.duncte123.botcommons.text.*;

public class ShutdownCommand extends ICommand {

	public ShutdownCommand() {
		this.name = "shutdown";
		this.ownerCommand = true;
		this.hidden = true;
		this.aliases = new String[] { "sd" };
		this.help = "Shuts down the bot. Can only be used by trusted users.";
		this.guildOnly = false;
		this.category = new ModerationCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		CavenBot.shouldShutdown = true;

		e.reply(":warning: Shutting down");
		e.reactWarning();

		e.getJDA().getShardManager().getShards().forEach((jda) -> {
			jda.getGuilds().forEach((guild) -> {
				if (admin.find(eq("guildid", guild.getId())).projection(include("voicereadymessage")).first()
						.getBoolean("voicereadymessage")) {
					String auditChannel = admin.find(eq("guildid", guild.getId())).projection(include("audits")).first()
							.getString("audits");
					JsonObject json = getLanguage(guild).getAsJsonObject("general").getAsJsonObject("shutdown");
					String message = json.get("message").getAsString();
					guild.getTextChannelById(auditChannel).sendMessage(message).queue();
				}
			});
		});

		logger.info(TextColor.ORANGE + "Shutting down " + TextColor.GREEN + e.getSelfUser().getAsTag()
				+ TextColor.ORANGE + " now" + TextColor.RESET);
		BotCommons.shutdown(e.getJDA().getShardManager());
		mongoClient.close();
	}
}
