package com.ktar5.jazzy.editor.tileset;

import com.ktar5.jazzy.editor.gui.dialogs.GenericAlert;
import com.ktar5.jazzy.editor.tilemap.BaseTilemap;
import com.ktar5.jazzy.editor.tilemap.whole.WholeTileset;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Tilesets {
    private BaseTilemap parent;
    private ArrayList<BaseTileset> tilesets;
    
    public Tilesets(BaseTilemap parent) {
        this.parent = parent;
        tilesets = new ArrayList<>();
    }
    
    /**
     * This method should check if the tileset exists within the jsonobject.
     * If it does, it should deserialize and load it
     *
     * @param json the JSONMObject representing the entire tilemap
     */
    protected void loadTileset(JSONObject json) {
        //TODO
        File tileset = Paths.get(getSaveFile().getPath()).resolve(json.getString("tileset")).toFile();
        WholeTileset tileset1 = TilesetManager.get().loadTileset(tileset, WholeTileset.class);
        if (tileset1.getTileHeight() != getTileHeight() || tileset1.getTileWidth() != getTileWidth()) {
            new GenericAlert("Tileset's tilesize does not match map's tilesize");
            return;
        }
        this.setTileset(tileset1);
    }
    
    public JSONArray serialize() {
        JSONArray json = new JSONArray();
        for (int i = 0; i < tilesets.size(); i++) {
            String path = Paths
                    .get(parent.getSaveFile().getPath())
                    .relativize(Paths.get(tilesets.get(i).getSaveFile().getPath()))
                    .toString();
            json.put(i, path);
        }
        return json;
    }
}
