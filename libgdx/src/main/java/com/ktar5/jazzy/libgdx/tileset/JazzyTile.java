package com.ktar5.jazzy.libgdx.tileset;

import com.ktar5.jazzy.libgdx.drawers.DrawWrapper;

/**
 * Heavily based off of LibGDX's TiledMapTile
 */
public abstract class JazzyTile<T extends JazzyTileset> {
    public static final int ROTATE_0 = 0;
    public static final int ROTATE_90 = 1;
    public static final int ROTATE_180 = 2;
    public static final int ROTATE_270 = 3;
    private boolean flipHorizontally;
    private boolean flipVertically;
    private int rotation;
    private BlendMode blendMode = BlendMode.ALPHA;
    private T tileset;
    
    public JazzyTile(T tileset) {
        this.tileset = tileset;
    }
    
    public abstract JazzyTile<T> deserialize(String data);
    
    /**
     * @return the {@link BlendMode} to use for rendering the tile
     */
    public BlendMode getBlendMode() {
        return blendMode;
    }
    
    /**
     * Sets the {@link BlendMode} to use for rendering the tile
     *
     * @param blendMode the blend mode to use for rendering the tile
     */
    public void setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
    }
    
    /**
     * @return Whether the tile should be flipped horizontally.
     */
    public boolean getFlipHorizontally() {
        return flipHorizontally;
    }
    
    /**
     * Sets whether to flip the tile horizontally.
     *
     * @param flipHorizontally whether or not to flip the tile horizontally.
     * @return this, for method chaining
     */
    public JazzyTile<T> setFlipHorizontally(boolean flipHorizontally) {
        this.flipHorizontally = flipHorizontally;
        return this;
    }
    
    /**
     * @return Whether the tile should be flipped vertically.
     */
    public boolean getFlipVertically() {
        return flipVertically;
    }
    
    /**
     * Sets whether to flip the tile vertically.
     *
     * @param flipVertically whether or not this tile should be flipped vertically.
     * @return this, for method chaining
     */
    public JazzyTile<T> setFlipVertically(boolean flipVertically) {
        this.flipVertically = flipVertically;
        return this;
    }
    
    /**
     * @return The rotation of this cell, in degrees.
     */
    public int getRotation() {
        return rotation;
    }
    
    /**
     * Sets the rotation of this cell, in degrees.
     *
     * @param rotation the rotation in degrees.
     * @return this, for method chaining
     */
    public JazzyTile<T> setRotation(int rotation) {
        this.rotation = rotation;
        return this;
    }
    
    //TODO consider if we need to input offset or not
    abstract void draw(DrawWrapper drawer);
    
    public enum BlendMode {
        NONE, ALPHA
    }
    
}
