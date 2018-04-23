package com.ktar5.jazzy.libgdx.drawers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BatchDrawWrapper implements DrawWrapper {
    private final Batch batch;
    
    public BatchDrawWrapper(Batch batch) {
        this.batch = batch;
    }
    
    @Override
    public void draw(TextureRegion region, float x, float y) {
        batch.draw(region, x, y);
    }
    
    @Override
    public void draw(TextureRegion region, float x, float y, float width, float height) {
        batch.draw(region, x, y, width, height);
    }
    
    @Override
    public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
        batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
    }
}
