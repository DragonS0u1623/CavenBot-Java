package com.dragons0u1;

import static com.dragons0u1.utils.StaticReferences.*;

import java.net.*;
import java.time.*;

import javax.security.auth.login.*;

import com.dragons0u1.commands.actions.*;
import com.dragons0u1.commands.anon.*;
import com.dragons0u1.commands.destiny.*;
import com.dragons0u1.commands.misc.*;
import com.dragons0u1.commands.misc.reminders.*;
import com.dragons0u1.commands.moderation.*;
import com.dragons0u1.commands.nsfw.*;
import com.dragons0u1.commands.roles.*;
import com.dragons0u1.events.*;
import com.dragons0u1.utils.*;
import com.jagrosh.jdautilities.command.*;
import com.jagrosh.jdautilities.commons.waiter.*;

import io.github.cdimascio.dotenv.*;
import me.duncte123.botcommons.messaging.*;
import me.duncte123.botcommons.text.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.*;
import net.dv8tion.jda.api.sharding.*;
import net.dv8tion.jda.api.utils.*;

public class CavenBot {
	private static CavenBot instance;
	private final ShardManager shardManager;
	public static boolean shouldShutdown = false;

	private CavenBot() throws LoginException {
		instance = this;
		Dotenv env = Dotenv.load();
		
		DefaultShardManagerBuilder shardBuilder = DefaultShardManagerBuilder.createDefault(env.get("BOT_KEY"))
				.enableIntents(GatewayIntent.GUILD_MEMBERS).setChunkingFilter(ChunkingFilter.ALL)
				.setMemberCachePolicy(MemberCachePolicy.ALL).setShardsTotal(-1).setAutoReconnect(true);
		shardManager = shardBuilder.build();

		EventWaiter waiter = new EventWaiter();

		EmbedUtils.setEmbedBuilder(() -> new EmbedBuilder().setFooter("Bot developed by Teddy Bear Inc\u2122#1525", 
				"https://cdn.discordapp.com/avatars/163667745580253184/4b04486f573d7378a9df875de5e20dc5.webp?size=1024").setTimestamp(Instant.now()));

		// ************************************* COMMANDS *****************************************************
		logger.info("Loading " + TextColor.CYAN + "commands" + TextColor.RESET + "...");
		CommandClientBuilder builder = initNormalCommands(waiter);
		shardManager.addEventListener(builder.build());

		// ***************************************** ANON *****************************************************
		builder = initAnonCommands();
		shardManager.addEventListener(builder.build());
		logger.info(TextColor.CYAN + "Commands" + TextColor.RESET + " loaded...");

		// ********************************* LISTENERS ********************************************************
		logger.info("Loading " + TextColor.YELLOW + "Listeners" + TextColor.RESET + "...");
		shardManager.addEventListener(waiter, new JoinLeaveListener(), new GuildJoinLeaveListener(),
				new ReadyListener(), new RoleEventListener());
		logger.info(TextColor.YELLOW + "Listeners" + TextColor.RESET + " loaded...");
		int shards = (shardManager.getShardsRunning() - shardManager.getShardsQueued());
		shardManager.setActivity(Activity.playing(String.format("m?help on %s", shards + " " + ((shards > 1) ? "shards" : "shard"))));
		logger.info("Logged in as " + TextColor.GREEN + shardManager.getShardById(0).getSelfUser().getAsTag());
	}

	public static CavenBot getInstance() {
		if (instance == null)
			try {
				instance = new CavenBot();
			} catch (LoginException e) {
				logger.fatal(TextColor.RED + "Error logging in." + TextColor.RESET);
				e.printStackTrace();
			}
		return instance;
	}

	public ShardManager getShardManager() {
		return shardManager;
	}

	public boolean isShutdown() {
		return shouldShutdown;
	}

	private CommandClientBuilder initNormalCommands(EventWaiter waiter) {
		CommandClientBuilder builder = new CommandClientBuilder();
		builder.setOwnerId(AUTHORID);
		builder.setCoOwnerIds(COOWNERS);
		builder.setAlternativePrefix("m?");
		builder.setHelpWord("help");
		builder.setHelpConsumer(new HelpConsumer());
		builder.setDiscordBotsKey(Dotenv.load().get("DBOTS_KEY"));
		builder.setServerInvite("https://discord.gg/6TjuPYy");
		builder.setEmojis("\u2705", "\u26A0", "\u274C");
		builder.setShutdownAutomatically(true);
		builder.addCommands(
				// ************************************** ACTIONS ********************************************
				new BeersCommand(), new BegCommand(), new BiteCommand(), new CakeCommand(), new CookieCommand(),
				new CuddleCommand(), new GifCommand(), new GlareCommand(), new HighFiveCommand(), new HugCommand(),
				new KissCommand(), new LickCommand(), new MuffinButtonCommand(), new PatCommand(), new PokeCommand(), 
				new PunchCommand(), new SassCommand(), new ShankCommand(), new SlapCommand(),

				// ************************************** DESTINY ********************************************
				new DestinyCommand(),

				// ********************************** MODERATION *********************************************
				new AuditCommand(), new BanCommand(), new ClearCommand(), new EditWelcomeCommand(), new KickCommand(),
				new SetLanguageCommand(), new ServerInfoCommand(), new ToggleSettings(), new UnbanCommand(), 
				new WelcomeCommand(),

				// ************************************** ROLES **********************************************
				new JoinRoleCommand(), new RoleReactCommand(),

				// ************************************** MISC ***********************************************
				new DefineCommand(), new DiceCommand(), new GeneralKenobiCommand(), new HelloThereCommand(), new InviteCommand(),
				new JokeCommand(), new JoinedGuildsCommand(), new MemeCommand(), new NaniCommand(), new OmaeCommand(), new PFPCommand(), 
				new PingCommand(), new ReminderListCommand(), new ReviewMeCommand(), new SupportServerCommand(), new UptimeCommand(),

				// ************************************* NSFW ************************************************
				new DomOrSubCommand(), new PornCommand(), new PunishmentCommand(), new ToyCommand(),

				// ************************************* HIDDEN **********************************************
				new ShutdownCommand()

		);
		logger.info(TextColor.CYAN + "Normal commands" + TextColor.RESET + " loaded...");
		return builder;
	}

	private CommandClientBuilder initAnonCommands() {
		CommandClientBuilder anonCommands = new CommandClientBuilder();
		anonCommands.setOwnerId(AUTHORID);
		anonCommands.setCoOwnerIds(COOWNERS);
		anonCommands.setPrefix("anon");
		anonCommands.setHelpConsumer(new HelpConsumer());
		anonCommands.setServerInvite("https://discord.gg/6TjuPYy");
		anonCommands.setEmojis("\u2705", "\u26A0", "\u274C");
		anonCommands.setShutdownAutomatically(true);
		anonCommands.addCommands(new AnonBlacklistCommand(), new AnonSendCommand(), new NoAnonCommand(), new RemoveBlacklistCommand());
		logger.info(TextColor.PURPLE + "Anon commands" + TextColor.RESET + " loaded...");
		return anonCommands;
	}
}
