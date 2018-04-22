package com.ktar5.jazzy.tilemaps;

import com.ktar5.jazzy.coordination.EditorCoordinator;
import com.ktar5.jazzy.gui.centerview.tabs.AbstractTab;
import com.ktar5.jazzy.gui.centerview.tabs.TilemapTab;
import com.ktar5.jazzy.properties.RootProperty;
import com.ktar5.jazzy.tileset.BaseTileset;
import com.ktar5.jazzy.tileset.Tile;
import com.ktar5.jazzy.util.Tabbable;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import com.ktar5.utilities.common.constants.Direction;
import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Getter
public abstract class BaseTilemap<S extends BaseTileset> implements Tabbable {
    private RootProperty rootProperty;
    private final int width, height, tileWidth, tileHeight;
    @Getter(AccessLevel.NONE)
    protected Tile[][] grid;
    private UUID id;
    private File saveFile;
    private S tileset;
    @Setter
    private boolean dragging;
    
    /**
     * This constructor is used to deserialize tilemaps.
     * It **MUST** be overriden by all subclasses.
     *
     * @param saveFile the file used for saving
     * @param json     the json serialization of the tilemap
     */
    public BaseTilemap(File saveFile, JSONObject json) {
        this(saveFile, json.getJSONObject("dimensions").getInt("width"),
                json.getJSONObject("dimensions").getInt("height"),
                json.getInt("tileWidth"), json.getInt("tileHeight"));
        rootProperty.deserialize(json.getJSONObject("properties"));
        
        loadTilesetIfExists(json);
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
    
    /**
     * This constructor is used to initialize tilemaps on creation.
     * It **MUST** be overrided by all subclasses.
     *
     * @param saveFile   the file used for saving
     * @param width      the width (in tiles) of the tilemap
     * @param height     the height (in tiles) of the tilemap
     * @param tileWidth  the width (in pixels) of a single tile
     * @param tileHeight the height (in pixels) of a single tile
     */
    public BaseTilemap(File saveFile, int width, int height, int tileWidth, int tileHeight) {
        this.width = width;
        this.height = height;
        this.saveFile = saveFile;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        this.grid = new Tile[width][height];
        this.rootProperty = new RootProperty();
        this.id = UUID.randomUUID();
    }
    
    /**
     * Gets an instance of the TilemapTab used to set the layout and display
     * the content of the tilemap
     *
     * @return a new instance of a TilemapTab unique to the implementation
     */
    public abstract TilemapTab getNewTilemapTab();
    
    /**
     * @return true if the x and y are within the bounds of the map
     */
    public boolean isInMapRange(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    /**
     * Expand the map by n tiles in a direction
     *
     * @param n         amount of tiles to expand
     * @param direction direction to place the new tiles
     */
    public void expandMap(int n, Direction direction) {
        int heightTmpMap = width + (direction.y * n);
        int widthTmpMap = height + (direction.x * n);
        Tile[][] tilemap = new Tile[widthTmpMap][heightTmpMap];
        
        // Copy the old map's data to the new one.
        for (int y = 0; y < grid.length; y++) {
            //TODO might need to fix
            System.arraycopy(grid[y], 0, tilemap[y + direction.y * n], direction.x * n, grid[0].length);
        }
        setChanged(true);
    }
    
    /**
     * Set the tileset of this tilemap to the passed parameter
     *
     * @param tileset the new tileset to set to this map
     */
    public void setTileset(S tileset) {
        this.tileset = tileset;
        AbstractTab currentTab = EditorCoordinator.get().getEditor().getCurrentTab();
        if (currentTab instanceof TilemapTab) {
            ((TilemapTab) currentTab).getTilesetSidebar().getTilesetView().setTileset(getTileset());
        }
    }
    
    /**
     * This method is called to deserialize each comma-separated string in the
     * "tilemap" section of the serialized json file
     *
     * @param block the string representing a single element of the comma-separated string in
     *              the serialized json file
     */
    protected abstract void deserializeBlock(String block, int x, int y);
    
    /**
     * This method should check if the tileset exists within the jsonobject.
     * If it does, it should deserialize and load it
     *
     * @param object the JSONMObject representing the entire tilemap
     */
    protected abstract void loadTilesetIfExists(JSONObject object);
    
    @Override
    @CallSuper
    /**
     * Serializes the information stored in the tilemap to a json file.
     * Subclasses should override this and provide specific implementations.
     * NOTE: all subclasses must call super or else code won't compile.
     */
    public JSONObject serialize() {
        JSONObject json = new JSONObject();
        
        getRootProperty().serialize(json);
        
        JSONObject dimensions = new JSONObject();
        dimensions.put("width", getWidth());
        dimensions.put("height", getHeight());
        json.put("dimensions", dimensions);
        
        JSONObject spawn = new JSONObject();
        json.put("spawn", spawn);
        
        json.put("tileWidth", getTileWidth());
        json.put("tileHeight", getTileHeight());
        
        JSONArray jsonArray = new JSONArray();
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y <= getHeight() - 1; y++) {
            for (int x = 0; x <= getWidth() - 1; x++) {
                if (grid[x][y] == null) {
                    builder.append("0");
                } else {
                    builder.append(grid[x][y].serialize());
                }
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            jsonArray.put(y, builder.toString());
            builder.setLength(0);
        }
        
        json.put("tilemap", jsonArray);
        return json;
    }
    
    @Override
    /**
     * Retrieves dimensions of the tilemap. Format is as follows:
     * pair(
     *      pair(total width, total height),
     *      pair(tile width, tile height)
     *   )
     */
    public Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> getDimensions() {
        return new Pair<>(new Pair<>(getTileWidth() * getWidth(), getTileHeight() * getHeight()), new Pair<>(getTileWidth(), getTileHeight()));
    }
    
    @Override
    /**
     * Saves the map to a file.
     */
    public void save() {
        MapManager.get().saveMap(getId());
    }
    
    @Override
    /**
     * Gets the name of the map.
     */
    public String getName() {
        return getSaveFile().getName();
    }
    
    @Override
    /**
     * Removes the map from the application.
     */
    public void remove() {
        MapManager.get().remove(getId());
    }
    
    @Override
    /**
     * Change the save file to a different file.
     */
    public void updateSaveFile(File file) {
        this.saveFile = file;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTilemap baseTilemap = (BaseTilemap) o;
        return width == baseTilemap.width &&
                height == baseTilemap.height &&
                tileHeight == baseTilemap.tileHeight &&
                tileWidth == baseTilemap.tileWidth &&
                Objects.equals(id, baseTilemap.id) &&
                Objects.equals(saveFile, baseTilemap.saveFile) &&
                Arrays.equals(grid, baseTilemap.grid);
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hash(saveFile, width, height, tileWidth, tileHeight);
        result = 31 * result + Arrays.hashCode(grid);
        return result;
    }
    
}
