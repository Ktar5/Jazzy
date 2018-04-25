package com.ktar5.jazzy.editor.tilemap;

import com.ktar5.jazzy.editor.util.Drawable;
import javafx.scene.layout.Pane;
import org.json.JSONArray;

import java.util.ArrayList;

public class Layers implements Drawable {
    private BaseTilemap parent;
    private ArrayList<BaseLayer> layers;
    
    public Layers(BaseTilemap parent) {
        this.parent = parent;
        layers = new ArrayList<>();
    }
    
    @Override
    public void draw(Pane pane) {
        for (int i = 0; i < layers.size(); i++) {
            layers.get(i).draw(pane);
        }
    }
    
    public JSONArray serialize() {
        JSONArray json = new JSONArray();
        for (int i = 0; i < layers.size(); i++) {
            json.put(i, layers.get(i).serialize());
        }
        return json;
    }
}
