package com.dragons0u1.commands.anon;

import static com.dragons0u1.Main.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import java.util.*;

import org.bson.*;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;
import com.mongodb.client.*;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.sharding.*;

public class AnonSendCommand extends ICommand {
	MongoCollection<Document> blacklist = database.getCollection("blacklist");

	public AnonSendCommand() {
		this.name = "send";
		this.arguments = "<channel/channel ID> <message>";
		this.help = "Sends an anonymous message to the given server channel.";
		this.guildOnly = false;
		this.category = new AnonCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		if (!e.isFromType(ChannelType.PRIVATE))
			return;

		if (e.getArgs().isBlank()) {
			e.reactError();
			return;
		}

		Message message = e.getMessage();
		String[] args = e.getArgs().split(" ", 2);
		JDA jda = e.getJDA();

		if (!message.getMentionedChannels().isEmpty()) {
			TextChannel channel = message.getMentionedChannels().get(0);
			if (jda.getTextChannels().contains(channel)) {
				if (!checkPerms(channel, e)) {
					e.reactError();
					e.reply("I can't send messages to that channel. It is either blacklisted, or you're not allowed to vent in that server.");
					return;
				}

				TextChannel textChannel;
				try {
					textChannel = jda.getTextChannelById(channel.getId());
					textChannel.sendMessage(args[1].replaceAll("@everyone", "everyone").replaceAll("@here", "here"))
							.queue();
					e.reactSuccess();
				} catch (Exception ex) {
					e.reactError();
				}
			} else {
				ShardManager manager = jda.getShardManager();
				for (JDA shard : manager.getShards()) {
					if (shard.getTextChannels().contains(channel)) {
						jda = shard;
						break;
					}
				}

				if (!checkPerms(channel, e)) {
					e.reactError();
					e.reply("I can't send messages to that channel. It is either blacklisted, or you're not allowed to vent in that server.");
					return;
				}

				TextChannel textChannel;
				try {
					textChannel = jda.getTextChannelById(channel.getId());
					textChannel.sendMessage(args[1].replaceAll("@everyone", "everyone").replaceAll("@here", "here"))
							.queue();
					e.reactSuccess();
				} catch (Exception ex) {
					e.reactError();
				}
			}
		} else {
			TextChannel channel = null;
			try {
				channel = e.getJDA().getShardManager().getTextChannelById(args[0]);
			} catch (Exception ex) {
				e.reactError();
				e.reply("I couldn't find that text channel. Please make sure that the ID is correct, or tag it with <#id> format");
			}

			if (jda.getTextChannels().contains(channel)) {
				if (!checkPerms(channel, e)) {
					e.reactError();
					e.reply("I can't send messages to that channel. It is either blacklisted, or you're not allowed to vent in that server.");
					return;
				}

				TextChannel textChannel;
				try {
					textChannel = jda.getTextChannelById(channel.getId());
					textChannel.sendMessage(args[1].replaceAll("@everyone", "everyone").replaceAll("@here", "here"))
							.queue();
					e.reactSuccess();
				} catch (Exception ex) {
					e.reactError();
				}
			} else {
				ShardManager manager = jda.getShardManager();
				for (JDA shard : manager.getShards()) {
					if (shard.getTextChannels().contains(channel)) {
						jda = shard;
						break;
					}
				}

				if (!checkPerms(channel, e)) {
					e.reactError();
					e.reply("I can't send messages to that channel. It is either blacklisted, or you're not allowed to vent in that server.");
					return;
				}

				TextChannel textChannel;
				try {
					textChannel = jda.getTextChannelById(channel.getId());
					textChannel.sendMessage(args[1].replaceAll("@everyone", "everyone").replaceAll("@here", "here"))
							.queue();
					e.reactSuccess();
				} catch (Exception ex) {
					e.reactError();
				}
			}
		}
	}

	private boolean checkPerms(TextChannel channel, CommandEvent e) {
		Member member = channel.getGuild().getMember(e.getAuthor());
		List<String> blacklistedChannels = new ArrayList<>();
		blacklist.find(eq("guildid", channel.getGuild().getId())).projection(include("channel"))
				.forEach(d -> blacklistedChannels.add(d.getString("channel")));
		boolean noVentRole = false;
		Role noVent = null;
		if (!channel.getGuild().getRolesByName("No Venting", true).isEmpty()) {
			noVent = channel.getGuild().getRolesByName("No Venting", true).get(0);
			noVentRole = true;
		}

		JsonObject error = getError(e.getAuthor(), channel.getGuild()).getAsJsonObject("anon");

		if (member == null) {
			e.reactError();
			String not_member = error.get("not_member").getAsString();
			e.reply(not_member);
			return false;
		} else if (noVentRole && member.getRoles().contains(noVent)) {
			e.reactError();
			String no_vent = error.get("no_vent").getAsString();
			e.reply(no_vent);
			return false;
		} else if (blacklistedChannels.contains(channel.getId())) {
			e.reactError();
			String blacklisted = error.get("blacklisted").getAsString();
			e.reply(blacklisted);
			return false;
		}

		return true;
	}
}
