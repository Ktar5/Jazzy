package com.ktar5.jazzy.libgdx.tileset;

import com.badlogic.gdx.graphics.Texture;
import lombok.Getter;

@Getter
public final class BaseTilesetLoadData {
    private final int tileWidth, tileHeight;
    private final int paddingVertical, paddingHorizontal;
    private final int offsetLeft, offsetUp;
    private final int columns, rows;
    private final Texture texture;
    
    public BaseTilesetLoadData(Texture texture, int tileWidth, int tileHeight, int paddingVertical, int paddingHorizontal, int offsetLeft,
                               int offsetUp, int columns, int rows) {
        this.texture = texture;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.paddingVertical = paddingVertical;
        this.paddingHorizontal = paddingHorizontal;
        this.offsetLeft = offsetLeft;
        this.offsetUp = offsetUp;
        this.columns = columns;
        this.rows = rows;
    }
}
