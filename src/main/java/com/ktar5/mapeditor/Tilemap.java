package com.ktar5.mapeditor;

import com.google.gson.JsonArray;
import com.ktar5.mapeditor.tiles.Tile;
import com.ktar5.mapeditor.tiles.TileBlock;
import lombok.Getter;
import lombok.Setter;

import java.util.Timer;
import java.util.TimerTask;

public class Tilemap {
    public Tile[][] grid;
    public final int width, height, id;
    @Getter
    @Setter
    private int xStart = 0, yStart = 0;

    public Tilemap(int width, int height, int id) {
        this.width = width;
        this.height = height;
        this.id = id;
        this.grid = new Tile[width][height];
    }

    public boolean isInMapRange(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public static Tilemap create(int width, int height, int id) {
        Tilemap tilemap = new Tilemap(width, height, id);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tilemap.grid[x][y] = new TileBlock(0, 0, x, y);
            }
        }
        return tilemap;
    }

    public JsonArray getJsonArray() {
        JsonArray jsonArray = new JsonArray(height);
        StringBuilder builder = new StringBuilder();
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x <= width - 1; x++) {
                builder.append(grid[x][y].serialize());
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            jsonArray.add(builder.toString());
            builder.setLength(0);
        }
        return jsonArray;
    }

}
