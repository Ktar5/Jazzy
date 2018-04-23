package com.ktar5.jazzy.libgdx.tileset;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class JazzyTilesets implements Iterable<JazzyTileset> {
    private Array<JazzyTileset> tilesets;
    
    /**
     * Creates an empty collection of tilesets.
     */
    public JazzyTilesets() {
        tilesets = new Array<JazzyTileset>();
    }
    
    /**
     * @param index index to get the desired {@link JazzyTileset} at.
     * @return tileset at index
     */
    public JazzyTileset getTileSet(int index) {
        return tilesets.get(index);
    }
    
    /**
     * @param name Name of the {@link JazzyTileset} to retrieve.
     * @return tileset with matching name, null if it doesn't exist
     */
    public JazzyTileset getTileSet(String name) {
        for (JazzyTileset tileset : tilesets) {
            if (name.equals(tileset.getName())) {
                return tileset;
            }
        }
        return null;
    }
    
    /**
     * @param tileset set to be added to the collection
     */
    public void addTileSet(JazzyTileset tileset) {
        tilesets.add(tileset);
    }
    
    /**
     * Removes tileset at index
     *
     * @param index index at which to remove a tileset.
     */
    public void removeTileSet(int index) {
        tilesets.removeIndex(index);
    }
    
    /**
     * @param tileset set to be removed
     */
    public void removeTileSet(JazzyTileset tileset) {
        tilesets.removeValue(tileset, true);
    }
    
    /**
     * @param id id of the {@link JazzyTile} to get.
     * @return tile with matching id, null if it doesn't exist
     */
    public TextureRegion getTexture(int id) {
        // The purpose of backward iteration here is to maintain backwards compatibility
        // with maps created with earlier versions of a shared tileset.  The assumption
        // is that the tilesets are in order of ascending firstgid, and by backward
        // iterating precedence for conflicts is given to later tilesets in the list,
        // which are likely to be the earlier version of any given gid.
        // See TiledMapModifiedExternalTilesetTest for example of this issue.
        for (int i = tilesets.size - 1; i >= 0; i--) {
            JazzyTileset tileset = tilesets.get(i);
            TextureRegion texture = tileset.getTexture(id);
            if (texture != null) {
                return texture;
            }
        }
        return null;
    }
    
    /**
     * @return iterator to tilesets
     */
    @Override
    public Iterator<JazzyTileset> iterator() {
        return tilesets.iterator();
    }
}
