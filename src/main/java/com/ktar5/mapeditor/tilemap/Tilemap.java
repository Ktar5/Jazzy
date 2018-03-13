package com.ktar5.mapeditor.tilemap;

import com.google.gson.JsonArray;
import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.tiles.Tile;
import com.ktar5.mapeditor.tiles.composite.CompositeTile;
import com.ktar5.mapeditor.tiles.whole.WholeTile;
import com.ktar5.utilities.common.constants.Direction;
import javafx.scene.canvas.Canvas;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.UUID;

@Getter
public class Tilemap {
    private UUID id;

    //File saving/loading stuff
    private String mapName;
    private File saveFile;

    //Tilemap variables
    @Getter(AccessLevel.NONE)
    Tile[][] grid;
    private final int width, height, tileSize;
    @Setter
    private int xStart = 0, yStart = 0;

    //The canvas for which to render on
    private Canvas canvas = new Canvas();


    public Tilemap(int width, int height, int tileSize, UUID id, File file) {
        this(width, height, tileSize, id);
        this.saveFile = file;
        this.mapName = saveFile.getName();
    }


    //This is used because during deserialization we do not have access
    // to the file until after the json is processed. Might come up with a
    // better way to handle this in the future
    Tilemap(int width, int height, int tileSize, UUID id) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.grid = new Tile[width][height];
        this.id = id;
        canvas.setWidth(width);
        canvas.setHeight(height);
        canvas.setCache(true);
        //TODO do this?....
        //getChildren().add(canvas);
    }

    public static Tilemap createEmpty(int width, int height, int tileSize, UUID id, File file) {
        Tilemap tilemap = new Tilemap(width, height, tileSize, id, file);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tilemap.grid[x][y] = Tile.AIR;
            }
        }
        return tilemap;
    }

    public void updateNameAndFile(File file) {
        this.saveFile = file;
        this.mapName = file.getName();
    }

    public boolean isInMapRange(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
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

    public void expandMap(int n, Direction direction) {
        int heightTmpMap = width + (direction.y * n);
        int widthTmpMap = height + (direction.x * n);
        Tile[][] tilemap = new Tile[widthTmpMap][heightTmpMap];

        // Copy the old map's data to the new one.
        for (int y = 0; y < grid.length; y++) {
            //TODO might need to fix
            System.arraycopy(grid[y], 0, tilemap[y + direction.y * n], direction.x * n, grid[0].length);
        }

    }

    public void save() {
        MapManager.get().saveMap(getId());
    }

    public void draw() {
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x <= width - 1; x++) {
                grid[x][y].draw(canvas, x, y);
            }
        }
    }

    public void set(int x, int y, Tile tile) {
        this.grid[x][y] = tile;
        setChanged(true);
    }

    public void remove(int x, int y) {
        this.grid[x][y] = Tile.AIR;
        setChanged(true);
    }

    public void setChanged(boolean value) {
        Main.root.getCenterView().getEditorViewPane().setChanges(getId(), value);
    }

}
