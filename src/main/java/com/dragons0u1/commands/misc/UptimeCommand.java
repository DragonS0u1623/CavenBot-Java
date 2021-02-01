package com.dragons0u1.commands.misc;

import java.lang.management.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.dragons0u1.utils.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import net.dv8tion.jda.api.entities.*;

public class UptimeCommand extends ICommand {

	public UptimeCommand() {
		this.name = "uptime";
		this.aliases = new String[] { "up" };
		this.help = "Gets the current runtime of the bot";
		this.guildOnly = false;
		this.category = new MiscCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject json;
		if (e.getChannelType().equals(ChannelType.PRIVATE))
			json = JsonParser.parseReader(LanguageUtils.getLanguageFile("en_US")).getAsJsonObject().getAsJsonObject("commands")
					.getAsJsonObject("misc").getAsJsonObject("uptime");
		else
			json = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands")
				.getAsJsonObject("misc").getAsJsonObject("uptime");
		String message = json.get("message").getAsString();

		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		long uptime = runtime.getUptime();
		long uptimeInSeconds = uptime / 1000, uptimeInHours = uptimeInSeconds / (60 * 60), uptimeInDays = uptimeInHours / 24,
				uptimeInMinutes = (uptimeInSeconds / 60) - (uptimeInHours * 60), 
				numberOfSeconds = uptimeInSeconds % 60;
		uptimeInHours %= 24;
		e.reply(String.format(message, uptimeInDays, uptimeInHours, uptimeInMinutes, numberOfSeconds));
	}
}
