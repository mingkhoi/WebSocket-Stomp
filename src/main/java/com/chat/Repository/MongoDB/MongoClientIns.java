package com.chat.Repository.MongoDB;

import java.util.HashMap;

import com.chat.PropertyManager.PropUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoClientIns {
  private static MongoClient _mongoClientIns; 

  public static MongoClient GetMongoClient() throws Exception {
    HashMap<String, String> database = PropUtils.GetMongoDBChat();

    if (MongoClientIns._mongoClientIns == null) {
      MongoClientIns._mongoClientIns = MongoClients.create(database.get("connection"));
    }
    return MongoClientIns._mongoClientIns; 
  }
}
