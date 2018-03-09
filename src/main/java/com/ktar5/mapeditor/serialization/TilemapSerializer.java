package com.ktar5.mapeditor.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ktar5.mapeditor.Tilemap;

import java.lang.reflect.Type;

public class TilemapSerializer implements JsonSerializer<Tilemap> {
    @Override
    public JsonElement serialize(Tilemap src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("height", src.height);
        json.addProperty("width", src.width);
        json.addProperty("id", src.id);
        json.addProperty("spawnX", src.getXStart());
        json.addProperty("spawnY", src.getYStart());
        json.add("grid", src.getJsonArray());

        return json;
    }
}
