package com.ktar5.mapeditor.tilemap;

import com.google.gson.*;
import com.ktar5.mapeditor.tiles.composite.CompositeTile;
import com.ktar5.mapeditor.tiles.whole.WholeTile;

import java.lang.reflect.Type;
import java.util.UUID;

public class TilemapDeserializer implements JsonDeserializer<Tilemap> {
    @Override
    public Tilemap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject()) {
            throw new RuntimeException("The json provided is not a json object");
        }
        JsonObject jsonObject = (JsonObject) json;
        Tilemap tilemap = new Tilemap(jsonObject.getAsJsonObject("dimensions").get("width").getAsInt(),
                jsonObject.getAsJsonObject("dimensions").get("height").getAsInt(),
                jsonObject.get("tileSize").getAsInt()
        );
        tilemap.setXStart(jsonObject.getAsJsonObject("spawn").get("x").getAsInt());
        tilemap.setYStart(jsonObject.getAsJsonObject("spawn").get("y").getAsInt());

        JsonArray grid = jsonObject.getAsJsonArray("tilemap");
        String[][] blocks = new String[grid.size()][];
        for (int i = 0; i < grid.size(); i++) {
            blocks[i] = grid.get(i).getAsString().split(",");
        }

        String block;
        for (int x = 0; x < tilemap.getWidth(); x++) {
            for (int y = 0; y < tilemap.getHeight(); y++) {
                block = blocks[x][y];
                if (block.charAt(0) == '[') {
                    tilemap.grid[x][y] = new CompositeTile(block);
                } else {
                    if (block.contains("_")) {
                        String[] split = block.split("_");
                        tilemap.grid[x][y] = new WholeTile(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
                    } else {
                        tilemap.grid[x][y] = new WholeTile(Integer.valueOf(block), 0);
                    }
                }
            }
        }

        return tilemap;
    }
}
