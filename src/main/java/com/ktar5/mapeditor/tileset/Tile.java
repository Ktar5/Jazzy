package com.ktar5.mapeditor.tileset;

import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.util.ToolSerializeable;
import lombok.Getter;

@Getter
public abstract class Tile implements ToolSerializeable {
    public abstract boolean isFoursquare();

    public abstract void draw(BaseTilemap baseTilemap, int x, int y);

}

