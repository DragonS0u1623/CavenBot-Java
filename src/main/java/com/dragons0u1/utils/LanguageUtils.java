package com.dragons0u1.utils;

import static com.dragons0u1.Main.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import java.io.*;
import java.util.*;

import org.bson.*;

import com.mongodb.client.*;
import com.mongodb.lang.*;

import net.dv8tion.jda.api.entities.*;

public class LanguageUtils {
	public static MongoCollection<Document> lang = database.getCollection("languages");

	private static final List<String> LANGUAGES = new ArrayList<>();
	private static final Map<String, String> LANGUAGES_MAP;

	static {
		try (InputStream in = LanguageUtils.class.getResourceAsStream("/languages/list.txt")) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null)
				LANGUAGES.add(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, String> m = new HashMap<>();
		for (String language : LANGUAGES)
			m.put(language, "/languages/" + language + ".json");

		LANGUAGES_MAP = Collections.unmodifiableMap(m);
	}

	/**
	 * Gets the default language for the guild. Can be overridden by the user settings.
	 * @param guild
	 * @return the default language of the guild
	 */
	@NonNull
	public static String getGuildLanguage(Guild guild) {
		Document doc = admin.find(eq("guildid", guild.getId())).projection(include("language")).first();
		if (doc == null || doc.getString("language") == null)
			return "en_US";
		else
			return doc.getString("language");
	}

	/**
	 * Gets the user's chosen language. Overrides the guild's language settings.
	 * @param user
	 * @return the language of the user
	 */
	@NonNull
	public static String getUserLanguage(User user) {
		Document doc = lang.find(eq("userid", user.getId())).projection(include("language")).first();
		if (doc == null )
			return null;
		else if (doc.getString("language") == null)
			return "en_US";
		else
			return doc.getString("language");
	}
	
	public static String getUserOrGuildLanguage(User user, Guild guild) {
		String lang = getUserLanguage(user);
		if (lang == null)
			lang = getGuildLanguage(guild);
		return lang;
	}

	/**
	 * Gets the language file and returns a reader for decoding
	 * @param language
	 * @return a reader of the language file
	 */
	public static BufferedReader getLanguageFile(String language) {
		InputStream in = LanguageUtils.class
				.getResourceAsStream(LANGUAGES_MAP.getOrDefault(language, "/languages/en_US.json"));
		return new BufferedReader(new InputStreamReader(in));
	}
	
	/**
	 * Gets all of the languages supported by the bot.
	 * @return
	 */
	public static String getLanguages() {
		InputStream in = LanguageUtils.class.getResourceAsStream("/languages/list.txt");
		String languagesTxt = "";
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			String line;
			while ((line = reader.readLine()) != null)
				languagesTxt += String.format("`%s`\n", line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return languagesTxt;
	}

	/**
	 * Checks to see if the language provided is one supported by the bot
	 * @param language
	 * @return true - if the language is supported
	 */
	public static boolean isValidLang(String language) {
		return LANGUAGES.contains(language);
	}
}
