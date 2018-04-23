package com.ktar5.jazzy.libgdx.whole;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ktar5.jazzy.libgdx.tileset.JazzyTile;

public class JazzyWholeTile implements JazzyTile {
    private int id;
    private BlendMode blendMode = BlendMode.ALPHA;
    private float offsetX;
    private float offsetY;
    private TextureRegion textureRegion;
    
    /**
     * Creates a static tile with the given region
     *
     * @param textureRegion the {@link TextureRegion} to use.
     */
    public JazzyWholeTile(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }
    
    /**
     * Copy constructor
     *
     * @param copy the StaticTiledMapTile to copy.
     */
    public JazzyWholeTile(JazzyWholeTile copy) {
        this.textureRegion = copy.textureRegion;
        this.id = copy.id;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public BlendMode getBlendMode() {
        return blendMode;
    }
    
    @Override
    public void setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
    }
    
    @Override
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
    
    @Override
    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }
    
    @Override
    public float getOffsetX() {
        return offsetX;
    }
    
    @Override
    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }
    
    @Override
    public float getOffsetY() {
        return offsetY;
    }
    
    @Override
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
    
}
