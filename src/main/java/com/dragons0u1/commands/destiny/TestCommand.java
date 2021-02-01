package com.dragons0u1.commands.destiny;

import static com.dragons0u1.commands.destiny.DestinyCommand.*;
import static com.dragons0u1.utils.StaticReferences.*;

import java.io.*;
import java.net.*;

import com.dragons0u1.commands.*;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.*;

public class TestCommand extends ICommand {
	String baseURL = "https://www.bungie.net/platform/Destiny/";

	public TestCommand() {
		this.name = "test";
		this.help = "A test command for the Destiny API. Returns item info for the Gjallarhorn";
	}

	@Override
	protected void execute(CommandEvent e) {
		try {
			URL url = new URL(baseURL + "Manifest/InventoryItem/1274330687/");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("X-API-KEY", APIKEY);
			int responseCode = con.getResponseCode();
			logger.info(String.format("Destiny API returned response of: %d %s", responseCode, con.getResponseMessage()));
			if (responseCode == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line, response = "";
				while ((line = in.readLine()) != null)
					response += line;
				in.close();

				JsonObject json = JsonParser.parseString(response).getAsJsonObject();
				json = json.getAsJsonObject("Response").getAsJsonObject("data");
				String itemName = json.getAsJsonObject("inventoryItem").get("itemName").getAsString(),
						tierTypeName = json.getAsJsonObject("inventoryItem").get("tierTypeName").getAsString(),
						description = json.getAsJsonObject("inventoryItem").get("itemDescription").getAsString();
				e.reply(String.format("Got the item: %s. Tier: %s, Description: %s", itemName, tierTypeName, description));
			}
		} catch (IOException ex) {
			e.reply("Error with the Destiny API. Couldn't get the item info.");
		}
	}
}
