package com.dragons0u1.commands.destiny;

import static com.dragons0u1.commands.destiny.DestinyCommand.*;
import static com.dragons0u1.utils.StaticReferences.*;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.time.*;
import java.util.*;
import java.util.List;

import com.dragons0u1.commands.*;
import com.dragons0u1.commands.categories.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

import me.duncte123.botcommons.messaging.*;
import net.dv8tion.jda.api.*;

public class Destiny1XurCommand extends ICommand {
	String baseURL = "https://www.bungie.net/platform/Destiny/";

	public Destiny1XurCommand() {
		this.name = "xur";
		this.help = "Gets the current inventory of Xur";
		this.category = new DestinyCategory();
	}

	@Override
	protected void execute(CommandEvent e) {
		JsonObject destiny = getLanguage(e.getAuthor(), e.getGuild()).getAsJsonObject("commands").getAsJsonObject("destiny").getAsJsonObject("xur");
		JsonObject error = getError(e.getAuthor(), e.getGuild());
		try {
			URL url = new URL("https://www.bungie.net/Platform/Destiny/Advisors/Xur/");
			logger.info("Attempting to connect to Destiny API");
			HttpURLConnection.setFollowRedirects(true);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-API-KEY", APIKEY);
			int responseCode = con.getResponseCode();
			logger.info(String.format("Destiny API returned response of: %d %s", responseCode, con.getResponseMessage()));
			if (responseCode == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line, response = "", errorStatus = "";
				while ((line = in.readLine()) != null)
					response += line;
				in.close();

				JsonObject json = JsonParser.parseString(response).getAsJsonObject();
				errorStatus = json.get("ErrorStatus").getAsString();

				if (errorStatus.equalsIgnoreCase("Success")) {
					json = json.getAsJsonObject("Response").getAsJsonObject("data");
					String title = destiny.get("title").getAsString(), description = destiny.get("description").getAsString();
					EmbedBuilder embed = EmbedUtils.embedMessageWithTitle(title, description + DateFormat.getDateInstance().format(Date.from(Instant.now())))
							.setColor(Color.WHITE);
					List<String> items = new ArrayList<>();
					for (JsonElement element : json.get("saleItemCategories").getAsJsonArray()) {
						if (element.getAsJsonObject().get("categoryTitle").getAsString().equalsIgnoreCase("Exotic Gear")) {
							JsonElement inventory = element.getAsJsonObject().get("saleItems");
							for (JsonElement inventoryItems : inventory.getAsJsonArray()) {
								JsonObject item = inventoryItems.getAsJsonObject().getAsJsonObject("item");
								String hashCode = item.get("itemHash").getAsString();
								items.add(hashCode);
							}
						}
					}
					logger.info("Xur has " + items.size() + " exotic items for sale");
					for (String itemHash : items) {
						URL itemURL = new URL(String.format("%sManifest/InventoryItem/%s/", baseURL, itemHash));
						con = (HttpURLConnection) itemURL.openConnection();
						con.setRequestMethod("GET");
						con.setRequestProperty("X-API-KEY", APIKEY);
						logger.info(String.format("Item returned response of: %d %s", con.getResponseCode(), con.getResponseMessage()));

						if (con.getResponseCode() == 200) {
							in = new BufferedReader(new InputStreamReader(con.getInputStream()));
							response = "";
							while ((line = in.readLine()) != null)
								response += line;
							in.close();
							JsonObject itemJson = JsonParser.parseString(response).getAsJsonObject();
							String itemName = itemJson.getAsJsonObject("Response").getAsJsonObject("data")
									.getAsJsonObject("inventoryItem").get("itemName").getAsString();
							String itemType = itemJson.getAsJsonObject("Response").getAsJsonObject("data")
									.getAsJsonObject("inventoryItem").get("itemTypeName").getAsString();
							embed.addField(itemName, itemType, false);
						}
					}

					if (embed.getFields().size() > 0)
						e.reply(embed.build());
					else {
						String no_item_info = error.getAsJsonObject("destiny").get("no_item_info").getAsString();
						e.reply(no_item_info);
					}
				} else if (errorStatus.equalsIgnoreCase("DestinyVendorNotFound")) {
					String vendor_not_here = error.getAsJsonObject("destiny").get("vendor_not_here").getAsString();
					e.reply(vendor_not_here);
					return;
				}
			}
		} catch (IOException ex) {
			String io = error.getAsJsonObject("destiny").get("io").getAsString();
			e.reply(io);
		}
	}
}
