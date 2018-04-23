package com.ktar5.jazzy.libgdx.map;

import com.ktar5.jazzy.libgdx.tileset.JazzyTile;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class JazzyMapLayer extends JazzyAbstractLayer {
    private int width;
    private int height;
    
    private float tileWidth;
    private float tileHeight;
    
    private JazzyTile[][] tiles;
    
    public JazzyMapLayer(JSONObject json) {
        this.width = json.getJSONObject("dimensions").getInt("width");
        this.height = json.getJSONObject("dimensions").getInt("height");
        this.tileWidth = json.getInt("tileWidth");
        this.tileHeight = json.getInt("tileHeight");
        this.tiles = new JazzyTile[width][height];
        
        this.loadProperties(json.getJSONObject("properties"));
        
        JSONArray grid = json.getJSONArray("tilemap");
        String[][] blocks = new String[grid.length()][];
        for (int i = 0; i < grid.length(); i++) {
            blocks[i] = grid.getString(i).split(",");
        }
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
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
    
    protected abstract void deserializeBlock(String data, int x, int y);
    
    /**
     * @return map's width in tiles
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * @return map's height in tiles
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * @return tiles' width in pixels
     */
    public float getTileWidth() {
        return tileWidth;
    }
    
    /**
     * @return tiles' height in pixels
     */
    public float getTileHeight() {
        return tileHeight;
    }
    
    /**
     * @param x X coordinate
     * @param y Y coordinate
     * @return {@link JazzyTile} at (x, y)
     */
    public JazzyTile getCell(int x, int y) {
        if(!isInMapRange(x, y)) return null;
        return tiles[x][y];
    }
    
    /**
     * Sets the {@link JazzyTile} at the given coordinates.
     *
     * @param x    X coordinate
     * @param y    Y coordinate
     * @param cell the {@link JazzyTile} to set at the given coordinates.
     */
    public void setCell(int x, int y, JazzyTile cell) {
        if(!isInMapRange(x, y)) return;
        tiles[x][y] = cell;
    }
    
    /**
     * @return true if the x and y are within the bounds of the map
     */
    public boolean isInMapRange(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    
}