package com.ktar5.jazzy.libgdx.drawers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface DrawWrapper {
    
    public void draw(TextureRegion region, float x, float y);
    
    public void draw(TextureRegion region, float x, float y, float width, float height);
    
    public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height,
                     float scaleX, float scaleY, float rotation);
    
    
}
