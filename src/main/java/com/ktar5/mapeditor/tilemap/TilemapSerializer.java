package com.ktar5.mapeditor.tilemap;

import com.google.gson.*;

import java.lang.reflect.Type;

public class TilemapSerializer implements JsonSerializer<Tilemap> {
    @Override
    public JsonElement serialize(Tilemap src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();

        JsonObject dimensions = new JsonObject();
        dimensions.addProperty("width", src.getWidth());
        dimensions.addProperty("height", src.getHeight());
        json.add("dimensions", dimensions);

        JsonObject spawn = new JsonObject();
        spawn.addProperty("x", src.getXStart());
        spawn.addProperty("y", src.getYStart());
        json.add("spawn", spawn);

        json.addProperty("tileSize", src.getTileSize());

        json.add("tilemap", src.getJsonArray());
        return json;
    }
}
