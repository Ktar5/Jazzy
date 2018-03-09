package com.ktar5.mapeditor.serialization;

import com.google.gson.*;
import com.ktar5.tilejump.tools.mapeditor.Tilemap;
import com.ktar5.tilejump.tools.mapeditor.tiles.TileBlock;
import com.ktar5.tilejump.tools.mapeditor.tiles.TileFoursquare;

import java.lang.reflect.Type;

public class TilemapDeserializer implements JsonDeserializer<Tilemap> {
    @Override
    public Tilemap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject()) {
            throw new RuntimeException("The json provided is not a json object");
        }
        JsonObject jsonObject = (JsonObject) json;
        Tilemap tilemap = new Tilemap(jsonObject.get("width").getAsInt(),
                jsonObject.get("height").getAsInt(),
                jsonObject.get("id").getAsInt());
        tilemap.setXStart(jsonObject.get("spawnX").getAsInt());
        tilemap.setYStart(jsonObject.get("spawnY").getAsInt());

        JsonArray grid = jsonObject.getAsJsonArray("grid");
        String[][] blocks = new String[grid.size()][];
        for (int i = 0; i < grid.size(); i++) {
            blocks[i] = grid.get(i).getAsString().split(",");
        }

        String block;
        for (int x = 0; x < tilemap.width; x++) {
            for (int y = 0; y < tilemap.height; y++) {
                block = blocks[x][y];
                if (block.charAt(0) == '[') {
                    tilemap.grid[x][y] = new TileFoursquare(x, y, block);
                } else {
                    if (block.contains("_")) {
                        String[] split = block.split("_");
                        tilemap.grid[x][y] = new TileBlock(Integer.valueOf(split[0]), Integer.valueOf(split[1]), x, y);
                    } else {
                        tilemap.grid[x][y] = new TileBlock(Integer.valueOf(block), 0, x, y);
                    }
                }
            }
        }

        return tilemap;
    }
}
