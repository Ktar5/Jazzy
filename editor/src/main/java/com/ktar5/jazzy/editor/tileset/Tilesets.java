package com.ktar5.jazzy.editor.tileset;

import com.ktar5.jazzy.editor.tilemap.BaseTilemap;
import org.json.JSONArray;
import org.json.JSONObject;

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
    protected void loadTileset(JSONObject json){
    
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
