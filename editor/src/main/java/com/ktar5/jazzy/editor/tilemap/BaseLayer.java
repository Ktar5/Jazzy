package com.ktar5.jazzy.editor.tilemap;

import com.ktar5.jazzy.editor.properties.RootProperty;
import com.ktar5.jazzy.editor.tileset.Tile;
import com.ktar5.jazzy.editor.util.Drawable;
import com.ktar5.jazzy.editor.util.Interactable;
import com.ktar5.utilities.common.constants.Direction;
import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Optional;

@Getter
public abstract class BaseLayer implements Drawable, Interactable {
    private BaseTilemap parent;
    private String name;
    private RootProperty rootProperty;
    
    private boolean visible;
    private int tileHeight, tileWidth;
    private int xOffset, yOffset;
    private int xPadding, yPadding;
    
    @Getter(AccessLevel.NONE)
    protected Tile[][] grid;
    
    public BaseLayer(BaseTilemap parent, JSONObject json) {
        this(parent,
                json.getString("name"), json.getBoolean("visible"),
                json.getJSONObject("dimensions").getInt("tileHeight"),
                json.getJSONObject("dimensions").getInt("tileWidth"),
                json.getJSONObject("dimensions").getInt("xOffset"),
                json.getJSONObject("dimensions").getInt("yOffset"),
                json.getJSONObject("dimensions").getInt("xPadding"),
                json.getJSONObject("dimensions").getInt("yPadding"));
        JSONArray grid = json.getJSONArray("tilemap");
        String[][] blocks = new String[grid.length()][];
        for (int i = 0; i < grid.length(); i++) {
            blocks[i] = grid.getString(i).split(",");
        }
        for (int x = 0; x < this.grid.length; x++) {
            for (int y = 0; y < this.grid[0].length; y++) {
                /*
                Note here that we switch the x and y variable because we store the data as
                an array of ROWS, and we load it as ROWS, so the first-dimension array is the ROWS
                and the 2nd dimension of the array is the COLUMNS
                See comments in BaseTilemap#getJsonArray on the line with:
                jsonArray.put(y, builder.toString());
                Hence, data is stored "y-value, row string"
                */
                deserializeBlock(blocks[y][x], x, y);
            }
        }
    }
    
    public BaseLayer(BaseTilemap parent, String name, boolean visible, int tileHeight, int tileWidth,
                     int xOffset, int yOffset, int xPadding, int yPadding) {
        this.parent = parent;
        this.name = name;
        this.rootProperty = new RootProperty();
        this.visible = visible;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xPadding = xPadding;
        this.yPadding = yPadding;
        Pair<Integer, Integer> xydimension = calculateTileCounts();
        this.grid = new Tile[xydimension.getKey()][xydimension.getValue()];
    }
    
    private Pair<Integer, Integer> calculateTileCounts() {
        int xTiles = ((parent.getWidth() - xOffset) / (tileWidth + xPadding)) - xPadding;
        int yTiles = ((parent.getHeight() - yOffset) / (tileHeight + yPadding)) - yPadding;
        return new Pair<>(xTiles, yTiles);
    }
    
    /**
     * This method is called to deserialize each comma-separated string in the
     * "tilemap" section of the serialized json file
     *
     * @param block the string representing a single element of the comma-separated string in
     *              the serialized json file
     */
    protected abstract void deserializeBlock(String block, int x, int y);
    
    public Optional<Tile> tileFromPoint(int x, int y) {
        if (!parent.isInMapRange(x, y)) {
            throw new RuntimeException("Point (" + x + ", " + y + ") is not in the bounds of map: " + parent.getName());
        }
        x = (x - xOffset) / (tileWidth + xPadding);
        y = (y - yOffset) / (tileHeight + yPadding);
        if (x < 0 || y < 0)
            return Optional.empty();
        else
            return Optional.of(grid[x][y]);
    }
    
    @Deprecated
    public void expandMap(int n, Direction direction) {
        int heightTmpMap = parent.getHeight() + (direction.y * n);
        int widthTmpMap = parent.getWidth() + (direction.x * n);
        Tile[][] tilemap = new Tile[widthTmpMap][heightTmpMap];
        
        // Copy the old map's data to the new one.
        for (int y = 0; y < grid.length; y++) {
            //TODO might need to fix
            System.arraycopy(grid[y], 0, tilemap[y + direction.y * n], direction.x * n, grid[0].length);
        }
        parent.setChanged(true);
    }
    
    public abstract JSONObject serialize();
    
    public int getWidth() {
        return parent.getWidth();
    }
    
    public int getHeight() {
        return parent.getHeight();
    }
    
    public boolean isDragging() {
        return parent.isDragging();
    }
    
}
