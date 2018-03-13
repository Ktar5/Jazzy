package com.ktar5.mapeditor.tilemap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class TilemapSerializer implements JsonSerializer<Tilemap> {
    @Override
    public JsonElement serialize(Tilemap src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("height", src.getHeight());
        json.addProperty("width", src.getWidth());
        json.addProperty("tileSize", src.getTileSize());
        json.addProperty("id", src.getId().toString());
        json.addProperty("spawnX", src.getXStart());
        json.addProperty("spawnY", src.getYStart());
        json.add("tilemap", src.getJsonArray());

        return json;
    }
}
