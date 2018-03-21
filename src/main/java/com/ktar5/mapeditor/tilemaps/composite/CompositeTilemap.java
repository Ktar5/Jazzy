package com.ktar5.mapeditor.tilemaps.composite;

import com.ktar5.mapeditor.Main;
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

    @Override
    public void deserializeBlock(String block, int x, int y) {
        this.grid[x][y] = new CompositeTile(getTileset(), block);
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
                //TODO
            }
        }
    }

    public void set(int x, int y, CompositeTile tile) {
        remove(x,y);
        //TODO
        this.grid[x][y] = tile;
        grid[x][y].draw(Main.root.getCenterView().getEditorViewPane().getTabDrawingPane(getId()),
                x * getTileSize(), y * getTileSize());
        setChanged(true);
    }

    public void set(int x, int y, WholeTile tile) {
        remove(x,y);
        this.grid[x][y] = tile;
        grid[x][y].draw(Main.root.getCenterView().getEditorViewPane().getTabDrawingPane(getId()),
                x * getTileSize(), y * getTileSize());
        setChanged(true);
    }

    public void remove(int x, int y) {
        this.grid[x][y].remove(Main.root.getCenterView().getEditorViewPane().getTabDrawingPane(getId()));
        this.grid[x][y] = null;
        setChanged(true);
    }

}
