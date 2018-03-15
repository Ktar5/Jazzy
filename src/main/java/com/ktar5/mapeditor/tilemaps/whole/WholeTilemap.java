package com.ktar5.mapeditor.tilemaps.whole;

import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tileset.Tile;
import com.ktar5.mapeditor.tileset.BaseTileset;
import lombok.Getter;
import org.json.JSONObject;

import java.io.File;

@Getter
public class WholeTilemap extends BaseTilemap<BaseTileset> {

    public WholeTilemap(File saveFile, JSONObject json) {
        super(saveFile, json);
    }

    public WholeTilemap(File saveFile, int width, int height, int tileSize) {
        super(saveFile, width, height, tileSize);
    }

    WholeTilemap(File saveFile, int width, int height, int tileSize, boolean empty) {
        super(saveFile, width, height, tileSize, empty);
    }

    @Override
    public void deserializeBlock(String block, int x, int y) {
        if (block.contains("_")) {
            String[] split = block.split("_");
            this.grid[x][y] = new WholeTile(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
        } else {
            this.grid[x][y] = new WholeTile(Integer.valueOf(block), 0);
        }
    }

    @Override
    public void setEmpty() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                grid[x][y] = WholeTile.AIR;
            }
        }
    }

    public void draw() {
        for (int y = getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x <= getWidth() - 1; x++) {
                grid[x][y].draw(this, x, y);
            }
        }
    }

    public void set(int x, int y, Tile tile) {
        this.grid[x][y] = tile;
        setChanged(true);
    }

    public void remove(int x, int y) {
        this.grid[x][y] = WholeTile.AIR;
        setChanged(true);
    }

}
