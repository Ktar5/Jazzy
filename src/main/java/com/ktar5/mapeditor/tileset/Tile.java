package com.ktar5.mapeditor.tileset;

import com.ktar5.mapeditor.util.ToolSerializeable;
import lombok.Getter;

@Getter
public abstract class Tile implements ToolSerializeable {
    public abstract boolean isFoursquare();
}

