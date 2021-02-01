package com.dragons0u1;

import static com.dragons0u1.utils.StaticReferences.*;

import org.bson.*;

import com.mongodb.client.*;

import io.github.cdimascio.dotenv.*;

public class Main {
	public static MongoClient mongoClient;
	public static MongoDatabase database;
	public static MongoCollection<Document> serverSettings, admin, support;
	public static CavenBot cavenBot;

	public static void main(String[] args) {
		logger.info("Logging into MongoDB");
		mongoClient = MongoClients.create(Dotenv.load().get("MONGO_SRV"));
		database = mongoClient.getDatabase("cavenbot");
		serverSettings = database.getCollection("serversettings");
		admin = database.getCollection("admin");
		support = database.getCollection("support");
		logger.info("Logged into MongoDB");
		logger.info("Loading bot");
		cavenBot = CavenBot.getInstance();
	}
}
