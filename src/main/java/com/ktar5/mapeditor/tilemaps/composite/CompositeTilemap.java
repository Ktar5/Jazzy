package com.ktar5.mapeditor.tilemaps.composite;

import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tilemaps.whole.WholeTile;
import javafx.scene.input.MouseEvent;
import org.json.JSONObject;

import java.io.File;

public class CompositeTilemap extends BaseTilemap<CompositeTileset> {
    protected CompositeTilemap(File saveFile, JSONObject json) {
        super(saveFile, json);
    }

    protected CompositeTilemap(File saveFile, int width, int height, int tileSize) {
        super(saveFile, width, height, tileSize);
    }

    protected CompositeTilemap(File saveFile, int width, int height, int tileSize, boolean empty) {
        super(saveFile, width, height, tileSize, empty);
    }

    @Override
    public void deserializeBlock(String block, int x, int y) {
        this.grid[x][y] = new CompositeTile(block);
    }

    @Override
    public void setEmpty() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                grid[x][y] = WholeTile.AIR;
            }
        }
    }

    @Override
    protected void loadTilesetIfExists(JSONObject object) {

    }

    @Override
    public void onClick(MouseEvent event) {

    }

    public void draw() {
        for (int y = getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x <= getWidth() - 1; x++) {
                grid[x][y].draw(this, x, y);
            }
        }
    }

    public void set(int x, int y, CompositeTile tile) {
        this.grid[x][y] = tile;
        setChanged(true);
    }

    public void set(int x, int y, WholeTile tile) {
        this.grid[x][y] = tile;
        setChanged(true);
    }

    public void remove(int x, int y) {
        this.grid[x][y] = WholeTile.AIR;
        setChanged(true);
    }

}
