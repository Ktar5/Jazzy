package com.ktar5.mapeditor.tilemap;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.tiles.Tile;
import com.ktar5.utilities.common.constants.Direction;
import javafx.scene.canvas.Canvas;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
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

    Tilemap(File saveFile, int width, int height, int tileSize) {
        this.width = width;
        this.height = height;
        this.saveFile = saveFile;
        this.mapName = saveFile.getName();
        this.tileSize = tileSize;
        this.grid = new Tile[width][height];
        this.id = UUID.randomUUID();
        canvas.setWidth(width);
        canvas.setHeight(height);
        canvas.setCache(true);
        //TODO do this?....
        //getChildren().add(canvas);
    }

    public static Tilemap createEmpty(int width, int height, int tileSize, File file) {
        Tilemap tilemap = new Tilemap(file, width, height, tileSize);
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

    public JSONArray getJsonArray() {
        JSONArray jsonArray = new JSONArray();
        StringBuilder builder = new StringBuilder();
        for (int y = 0 ; y <= height -1 ; y++) {
            for (int x = 0; x <= width - 1; x++) {
                builder.append(grid[x][y].serialize());
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            jsonArray.put(y, builder.toString());
            builder.setLength(0);
        }
        return jsonArray;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tilemap tilemap = (Tilemap) o;
        return width == tilemap.width &&
                height == tilemap.height &&
                tileSize == tilemap.tileSize &&
                xStart == tilemap.xStart &&
                yStart == tilemap.yStart &&
                Objects.equals(id, tilemap.id) &&
                Objects.equals(mapName, tilemap.mapName) &&
                Objects.equals(saveFile, tilemap.saveFile) &&
                Arrays.equals(grid, tilemap.grid);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mapName, saveFile, width, height, tileSize, xStart, yStart);
        result = 31 * result + Arrays.hashCode(grid);
        return result;
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
