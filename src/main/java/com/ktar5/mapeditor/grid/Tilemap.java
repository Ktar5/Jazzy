package com.ktar5.mapeditor.grid;

import com.google.gson.JsonArray;
import com.ktar5.mapeditor.tiles.Tile;
import com.ktar5.utilities.common.constants.Direction;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Tilemap extends Pane {
    //The grid of tiles
    public Tile[][] grid;
    //The width, height, id, and tileSize properties
    private final int width, height, id, tileSize;
    //The spawn point coordinates
    @Setter
    private int xStart = 0, yStart = 0;
    //The canvas for which to render on
    private Canvas canvas = new Canvas();


    //The constructor
    public Tilemap(int width, int height, int tileSize, int id) {
        this.width = width;
        this.height = height;
        this.id = id;
        this.tileSize = tileSize;
        this.grid = new Tile[width][height];

        canvas.setWidth(width);
        canvas.setHeight(height);
        canvas.setCache(true);
        getChildren().add(canvas);
    }

    public static Tilemap createEmpty(int width, int height, int tileSize, int id) {
        Tilemap tilemap = new Tilemap(width, height, tileSize, id);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tilemap.grid[x][y] = Tile.AIR;
            }
        }
        return tilemap;
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
        MapManager.get().saveMap(id);
    }

    public void draw() {
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x <= width - 1; x++) {
                canvas.getGraphicsContext2D().drawImage();
                Image image;
                canvas.getGraphicsContext2D().rotate(45);
                canvas.getGraphicsContext2D().drawImage(element.getImage(), element.getX(), element.getY());
            }
        }
    }

    public void remove(int x, int y) {
        this.grid[x][y] = Tile.AIR;
    }


}
