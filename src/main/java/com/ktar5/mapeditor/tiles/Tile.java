package com.ktar5.mapeditor.tiles;

import com.ktar5.mapeditor.tiles.whole.WholeTile;
import com.ktar5.mapeditor.util.ToolSerializeable;
import lombok.Getter;

@Getter
public abstract class Tile implements ToolSerializeable {
    public static final WholeTile AIR = new WholeTile(0, 0);

    public abstract boolean isFoursquare();

}

