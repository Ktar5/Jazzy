package com.ktar5.mapeditor.tiles;

import com.ktar5.mapeditor.util.ToolSerializeable;
import lombok.Getter;

@Getter
public abstract class Tile implements ToolSerializeable {
    public transient final int x, y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract boolean isFoursquare();

}

