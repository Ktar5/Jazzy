package com.ktar5.jazzy.libgdx.map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.ktar5.jazzy.libgdx.properties.RootProperty;
import com.ktar5.jazzy.libgdx.tileset.JazzyTileset;
import org.json.JSONObject;

public abstract class JazzyMap implements Disposable {
    private JazzyMapLayers layers = new JazzyMapLayers();
    private RootProperty rootProperty = new RootProperty();
    
    private JazzyTileset tileset;
    private Array<? extends Disposable> ownedResources;
    
    /**
     * Creates an empty JazzyMap.
     */
    public JazzyMap(JSONObject json) {
        rootProperty.deserialize(json.getJSONObject("properties"));
        //TODO implement multiple map layers!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!/
        //tODO implement multiple map tilesets
        //TODO
        //TODO
        //todo
        loadMapLayers();
        
        
    }
    
    /**
     * @return collection of tilesets for this map.
     */
    public JazzyTileset getTileSet() {
        return tileset;
    }
    
    public JazzyMapLayers getLayers() {
        return layers;
    }
    
    public RootProperty getProperties() {
        return rootProperty;
    }
    
    /**
     * Used by loaders to set resources when loading the map directly, without {@link com.badlogic.gdx.assets.AssetManager}. To be disposed in
     * {@link #dispose()}.
     *
     * @param resources
     */
    public void setOwnedResources(Array<? extends Disposable> resources) {
        this.ownedResources = resources;
    }
    
    @Override
    public void dispose() {
        if (ownedResources != null) {
            for (Disposable resource : ownedResources) {
                resource.dispose();
            }
        }
    }
    
    
}
