package com.ktar5.jazzy.libgdx.tileset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.ktar5.jazzy.libgdx.properties.RootProperty;
import org.json.JSONObject;

import java.util.Iterator;

public abstract class JazzyTileset implements Iterable<TextureRegion> {
    private final String name = "";
    private final IntMap<TextureRegion> textures;
    private RootProperty rootProperty;
    private int tileWidth, tileHeight;
    
    
    public JazzyTileset(JSONObject json) {
        int paddingVertical = json.getJSONObject("padding").getInt("vertical");
        int paddingHorizontal = json.getJSONObject("padding").getInt("horizontal");
        int offsetLeft = json.getJSONObject("offset").getInt("left");
        int offsetUp = json.getJSONObject("offset").getInt("up");
        
        tileWidth = json.getInt("tileWidth");
        tileHeight = json.getInt("tileHeight");
        rootProperty = new RootProperty(json.getJSONObject("properties"));
        
        Texture texture = new Texture(Gdx.files.internal(json.getString("sourceFile")));
        int columns = (texture.getWidth() - offsetLeft) / (tileWidth + paddingHorizontal);
        int rows = (texture.getHeight() - offsetUp) / (tileHeight + paddingVertical);
        
        textures = getTilesetImages(texture, tileWidth, tileHeight, paddingVertical, paddingHorizontal,
                offsetLeft, offsetUp, columns, rows);
    }
    
    protected abstract IntMap<TextureRegion> getTilesetImages(Texture texture, int tileWidth, int tileHeight,
                                                              int paddingVertical, int paddingHorizontal, int offsetLeft,
                                                              int offsetUp, int columns, int rows);
    
    public TextureRegion getTexture(int id) {
        return textures.get(id);
    }
    
    /**
     * @return tileset's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return iterator to tiles in this tileset
     */
    @Override
    public Iterator<TextureRegion> iterator() {
        return textures.values().iterator();
    }
    
    /**
     * @return the size of this tileset.
     */
    public int size() {
        return textures.size;
    }
}
