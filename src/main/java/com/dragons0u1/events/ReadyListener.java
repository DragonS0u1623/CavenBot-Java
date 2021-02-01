package com.dragons0u1.events;

import static com.dragons0u1.utils.LanguageUtils.*;
import static com.dragons0u1.utils.StaticReferences.*;
import static com.dragons0u1.Main.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import com.google.gson.*;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.sharding.*;

public class ReadyListener extends ListenerAdapter {
	@Override
	public void onReady(ReadyEvent e) {
		logger.debug(String.format("Logged in as %s on shard %s", e.getJDA().getSelfUser().getAsTag(),
				e.getJDA().getShardInfo().getShardId()));
		JsonObject json;
		for (Guild guild : e.getJDA().getGuilds()) {
			json = JsonParser.parseReader(getLanguageFile(getGuildLanguage(guild))).getAsJsonObject()
					.getAsJsonObject("general");
			if (admin.find(eq("guildid", guild.getId())).projection(include("voicereadymessage")).first()
					.getBoolean("voicereadymessage")) {
				String auditChannel = admin.find(eq("guildid", guild.getId())).projection(include("audits")).first()
						.getString("audits");
				guild.getTextChannelById(auditChannel).sendMessage(json.get("ready").getAsString()).queue();
			}
		}

		ShardManager shardManager = e.getJDA().getShardManager();
		int shards = (shardManager.getShardsRunning() - shardManager.getShardsQueued());
		shardManager.setActivity(Activity.playing(String.format("m?help on %s", shards + " " + ((shards > 1) ? "shards" : "shard"))));
	}

	@Override
	public void onReconnect(ReconnectedEvent e) {
		logger.debug(String.format("Reconnected to %s on shard %s", e.getJDA().getSelfUser().getAsTag(),
				e.getJDA().getShardInfo().getShardId()));
		ShardManager shardManager = e.getJDA().getShardManager();
		int shards = (shardManager.getShardsRunning() - shardManager.getShardsQueued());
		shardManager.setActivity(Activity.playing(String.format("m?help on %s", shards + " " + ((shards > 1) ? "shards" : "shard"))));
	}

	@Override
	public void onDisconnect(DisconnectEvent e) {
		logger.debug(String.format("Disconnected from %s on shard %s", e.getJDA().getSelfUser().getAsTag(),
				e.getJDA().getShardInfo().getShardId()));
		logger.debug(String.format("Currently running on %s shards",
				(e.getJDA().getShardManager().getShardsRunning() - e.getJDA().getShardManager().getShardsQueued())));
		ShardManager shardManager = e.getJDA().getShardManager();
		int shards = (shardManager.getShardsRunning() - shardManager.getShardsQueued());
		shardManager.setActivity(Activity.playing(String.format("m?help on %s", shards + " " + ((shards > 1) ? "shards" : "shard"))));
	}

	@Override
	public void onResume(ResumedEvent e) {
		logger.debug("Reconnected on shard " + e.getJDA().getShardInfo().getShardId());
		ShardManager shardManager = e.getJDA().getShardManager();
		int shards = (shardManager.getShardsRunning() - shardManager.getShardsQueued());
		shardManager.setActivity(Activity.playing(String.format("m?help on %s", shards + " " + ((shards > 1) ? "shards" : "shard"))));
	}
}
