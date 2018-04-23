package com.ktar5.jazzy.libgdx.drawers;

import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CachedDrawWrapper implements DrawWrapper {
    private final SpriteCache cache;
    
    public CachedDrawWrapper(SpriteCache cache) {
        this.cache = cache;
    }
    
    @Override
    public void draw(TextureRegion region, float x, float y) {
        cache.add(region, x, y);
    }
    
    @Override
    public void draw(TextureRegion region, float x, float y, float width, float height) {
        cache.add(region, x, y, width, height);
    }
    
    @Override
    public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
        cache.add(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
    }
}
