package com.dragons0u1.events;

import static com.dragons0u1.Main.*;
import static com.dragons0u1.utils.LanguageUtils.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import java.util.*;

import org.bson.*;

import com.google.gson.*;
import com.mongodb.client.*;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.*;
import net.dv8tion.jda.api.hooks.*;

public class RoleEventListener extends ListenerAdapter {

	MongoCollection<Document> reactroles = database.getCollection("reactroles");

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent e) {
		if (e.getMember() == null || e.getUser() == null)
			return;
		if (e.getUser().isBot())
			return;
		if (e.getChannelType().equals(ChannelType.PRIVATE))
			return;
		if (reactroles.find(and(eq("guildid", e.getGuild().getId()), eq("messageid", e.getMessageId()))).first() == null)
			return;

		JsonObject json = JsonParser.parseReader(getLanguageFile(getUserOrGuildLanguage(e.getUser(), e.getGuild()))).getAsJsonObject().getAsJsonObject("commands")
				.getAsJsonObject("roles");
		String add_dm = json.get("add_dm").getAsString();

		e.retrieveMessage().queue((m) -> {
			Member member = e.getMember();
			boolean isEmoji = e.getReactionEmote().isEmoji();
			
			List<Document> docs = new ArrayList<>();
			List<String> roleids = new ArrayList<>();
			reactroles.find(and(eq("guildid", e.getGuild().getId()), eq("messageid", e.getMessageId())))
					.projection(include("messageid", "emoji", "roleid", "toggle")).forEach(doc -> {
						docs.add(doc);
						roleids.add(doc.getString("roleid"));
					});
			Document settings = serverSettings.find(eq("guildid", e.getGuild().getId())).projection(include("roledm"))
					.first();

			for (Document doc : docs) {
				if (isEmoji) {
					if (doc.getString("emoji").equals(e.getReactionEmote().getEmoji())) {
						Role role = e.getGuild().getRoleById(doc.getString("roleid"));
						e.getGuild().addRoleToMember(member, role).queue();
						if (doc.getBoolean("toggle")) {
							for (Role r : member.getRoles()) {
								if (roleids.contains(r.getId()) && !r.getId().equals(role.getId())) {
									e.getGuild().removeRoleFromMember(member, r).queue();
									m.getReactions().forEach((react) -> {
										if (!react.getReactionEmote().equals(e.getReactionEmote()))
											react.removeReaction(member.getUser()).queue();
									});
								}
							}
						}
						if (settings.getBoolean("roledm", true))
							member.getUser().openPrivateChannel().queue((ch) -> ch.sendMessage(
									String.format(add_dm, role.getName(), e.getGuild().getName()))
									.queue());
						break;
					}
				} else {
					if (doc.getString("emoji").equals(e.getReactionEmote().getEmote().getId())) {
						Role role = e.getGuild().getRoleById(doc.getString("roleid"));
						e.getGuild().addRoleToMember(member, role).queue();
						if (doc.getBoolean("toggle")) {
							List<Role> roles = member.getRoles();
							for (Role r : roles) {
								if (roleids.contains(r.getId()) && !r.getId().equals(role.getId())) {
									e.getGuild().removeRoleFromMember(member, r);
									m.getReactions().forEach((react) -> {
										if (!react.getReactionEmote().equals(e.getReactionEmote()))
											react.removeReaction(member.getUser()).queue();
									});
								}
							}
						}
						if (settings.getBoolean("roledm", true))
							member.getUser().openPrivateChannel().queue((ch) -> ch.sendMessage(
									String.format(add_dm, role.getName(), e.getGuild().getName()))
									.queue());
						break;
					}
				}
			}
			docs.clear();
			roleids.clear();
		});
	}

	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
		if (e.getMember() == null || e.getUser() == null)
			return;
		if (e.getUser().isBot())
			return;
		if (e.getChannelType().equals(ChannelType.PRIVATE))
			return;
		if (reactroles.find(and(eq("guildid", e.getGuild().getId()), eq("messageid", e.getMessageId()))).first() == null)
			return;
		
		JsonObject json = JsonParser.parseReader(getLanguageFile(getUserOrGuildLanguage(e.getUser(), e.getGuild()))).getAsJsonObject().getAsJsonObject("commands")
				.getAsJsonObject("roles");
		String remove_dm = json.get("remove_dm").getAsString();

		e.retrieveMessage().queue((m) -> {
			Member member = e.getMember();
			boolean isEmoji = e.getReactionEmote().isEmoji();
			if (member.getRoles().isEmpty())
				return;

			List<Document> docs = new ArrayList<>();
			reactroles.find(and(eq("guildid", e.getGuild().getId()), eq("messageid", m.getId())))
					.projection(include("emoji", "roleid")).forEach(doc -> docs.add(doc));

			Document settings = serverSettings.find(eq("guildid", e.getGuild().getId())).projection(include("roledm"))
					.first();

			for (Document doc : docs) {
				if (isEmoji) {
					if (doc.getString("emoji").equals(e.getReactionEmote().getEmoji())) {
						Role role = e.getGuild().getRoleById(doc.getString("roleid"));
						e.getGuild().removeRoleFromMember(member, role).queue();
						if (settings.getBoolean("roledm", true))
							member.getUser().openPrivateChannel().queue((ch) -> ch.sendMessage(
									String.format(remove_dm, role.getName(), e.getGuild().getName()))
									.queue());
						break;
					}
				} else {
					if (doc.getString("emoji").equals(e.getReactionEmote().getEmote().getId())) {
						Role role = e.getGuild().getRoleById(doc.getString("roleid"));
						e.getGuild().removeRoleFromMember(member, role).queue();
						if (settings.getBoolean("roledm", true))
							member.getUser().openPrivateChannel().queue((ch) -> ch.sendMessage(
									String.format(remove_dm, role.getName(), e.getGuild().getName()))
									.queue());
						break;
					}
				}
			}
			docs.clear();
		});
	}
}
