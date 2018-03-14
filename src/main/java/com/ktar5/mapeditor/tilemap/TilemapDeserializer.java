package com.ktar5.mapeditor.tilemap;

import com.ktar5.mapeditor.tiles.composite.CompositeTile;
import com.ktar5.mapeditor.tiles.whole.WholeTile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class TilemapDeserializer {
    public static Tilemap deserialize(File file, JSONObject json) {
        Tilemap tilemap = new Tilemap(file,
                json.getJSONObject("dimensions").getInt("width"),
                json.getJSONObject("dimensions").getInt("height"),
                json.getInt("tileSize")
        );
        tilemap.setXStart(json.getJSONObject("spawn").getInt("x"));
        tilemap.setYStart(json.getJSONObject("spawn").getInt("y"));

        JSONArray grid = json.getJSONArray("tilemap");
        String[][] blocks = new String[grid.length()][];
        for (int i = 0; i < grid.length(); i++) {
            blocks[i] = grid.getString(i).split(",");
        }

        //Because of how were doing the above we need to switch x and y
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
