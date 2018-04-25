package com.ktar5.jazzy.editor.tilemap;

import com.ktar5.jazzy.editor.gui.centerview.tabs.TilemapTab;
import com.ktar5.jazzy.editor.properties.RootProperty;
import com.ktar5.jazzy.editor.tileset.Tilesets;
import com.ktar5.jazzy.editor.util.Tabbable;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

@Getter
public abstract class BaseTilemap implements Tabbable {
    private Layers layers;
    private Tilesets tilesets;
    private RootProperty rootProperty;
    
    private final int width, height;
    private UUID id;
    private File saveFile;
    @Setter
    private boolean dragging;
    
    /**
     * This constructor is used to deserialize tilemap.
     * It **MUST** be overriden by all subclasses.
     *
     * @param saveFile the file used for saving
     * @param json     the json serialization of the tilemap
     */
    public BaseTilemap(File saveFile, JSONObject json) {
        this(saveFile, json.getJSONObject("dimensions").getInt("width"),
                json.getJSONObject("dimensions").getInt("height"));
        rootProperty.deserialize(json.getJSONObject("properties"));
    }
    
    /**
     * This constructor is used to initialize tilemap on creation.
     * It **MUST** be overrided by all subclasses.
     *
     * @param saveFile the file used for saving
     * @param width    the width (in pixels) of the tilemap
     * @param height   the height (in pixels) of the tilemap
     */
    public BaseTilemap(File saveFile, int width, int height) {
        this.width = width;
        this.height = height;
        this.saveFile = saveFile;
        this.rootProperty = new RootProperty();
        this.id = UUID.randomUUID();
        this.layers = new Layers(this);
        this.tilesets = new Tilesets(this);
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
        
        json.put("layers", layers.serialize());
        return json;
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
        
        BaseTilemap that = (BaseTilemap) o;
        
        if (width != that.width) return false;
        if (height != that.height) return false;
        if (dragging != that.dragging) return false;
        if (!layers.equals(that.layers)) return false;
        if (!rootProperty.equals(that.rootProperty)) return false;
        if (!id.equals(that.id)) return false;
        return saveFile.equals(that.saveFile);
    }
    
    @Override
    public int hashCode() {
        int result = layers.hashCode();
        result = 31 * result + rootProperty.hashCode();
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + id.hashCode();
        result = 31 * result + saveFile.hashCode();
        result = 31 * result + (dragging ? 1 : 0);
        return result;
    }
}
