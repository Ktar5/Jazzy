package com.ktar5.mapeditor.tilemaps;

import com.ktar5.mapeditor.tileset.BaseTileset;
import com.ktar5.mapeditor.tileset.Tile;
import com.ktar5.mapeditor.util.Tabbable;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import com.ktar5.utilities.common.constants.Direction;
import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Getter
public abstract class BaseTilemap<S extends BaseTileset> implements Tabbable {
    private UUID id;
    private File saveFile;

    @Getter(AccessLevel.NONE)
    protected Tile[][] grid;
    private final int width, height, tileSize;

    @Setter
    private int xStart = 0, yStart = 0;
    private S tileset;

    protected BaseTilemap(File saveFile, JSONObject json) {
        this(saveFile, json.getJSONObject("dimensions").getInt("width"),
                json.getJSONObject("dimensions").getInt("height"),
                json.getInt("tileSize"));
        this.xStart = json.getJSONObject("spawn").getInt("x");
        this.yStart = json.getJSONObject("spawn").getInt("y");
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
                if (this.grid[x][y] == null) {
                    Logger.info("Loading map: " + saveFile.getName() + ". Null found at: " + x + ", " + y);
                    MapManager.get().remove(getId());
                    return;
                }
            }
        }

    }

    protected BaseTilemap(File saveFile, int width, int height, int tileSize) {
        this(saveFile, width, height, tileSize, false);
    }

    protected BaseTilemap(File saveFile, int width, int height, int tileSize, boolean empty) {
        this.width = width;
        this.height = height;
        this.saveFile = saveFile;
        this.tileSize = tileSize;
        this.grid = new Tile[width][height];
        this.id = UUID.randomUUID();
        if (empty) {
            setEmpty();
        }
    }

    public boolean isInMapRange(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
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
        setChanged(true);
    }

    public void setTileset(S tileset) {
        this.tileset = tileset;
    }

    protected abstract void deserializeBlock(String block, int x, int y);

    protected abstract void setEmpty();

    protected abstract void loadTilesetIfExists(JSONObject object);

    @Override
    @CallSuper
    public JSONObject serialize() {
        JSONObject json = new JSONObject();

        JSONObject dimensions = new JSONObject();
        dimensions.put("width", getWidth());
        dimensions.put("height", getHeight());
        json.put("dimensions", dimensions);

        JSONObject spawn = new JSONObject();
        spawn.put("x", getXStart());
        spawn.put("y", getYStart());
        json.put("spawn", spawn);

        json.put("tileSize", getTileSize());

        JSONArray jsonArray = new JSONArray();
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y <= getHeight() - 1; y++) {
            for (int x = 0; x <= getWidth() - 1; x++) {
                builder.append(grid[x][y].serialize());
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
    public Pair<Integer, Integer> getDimensions() {
        return new Pair<>(getTileSize() * getWidth(), getTileSize() * getHeight());
    }

    @Override
    public void save() {
        MapManager.get().saveMap(getId());
    }

    @Override
    public String getName() {
        return getSaveFile().getName();
    }

    @Override
    public void remove() {
        MapManager.get().remove(getId());
    }

    @Override
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
                tileSize == baseTilemap.tileSize &&
                xStart == baseTilemap.xStart &&
                yStart == baseTilemap.yStart &&
                Objects.equals(id, baseTilemap.id) &&
                Objects.equals(saveFile, baseTilemap.saveFile) &&
                Arrays.equals(grid, baseTilemap.grid);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(saveFile, width, height, tileSize, xStart, yStart);
        result = 31 * result + Arrays.hashCode(grid);
        return result;
    }

}
