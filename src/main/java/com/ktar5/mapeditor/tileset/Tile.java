package com.ktar5.mapeditor.tileset;

import com.ktar5.mapeditor.util.ToolSerializeable;
import javafx.scene.layout.Pane;
import lombok.Getter;

@Getter
public abstract class Tile<T extends BaseTileset> implements ToolSerializeable {
    private T tileset;

    public Tile(T tileset) {
        this.tileset = tileset;
    }

    public abstract void remove(Pane pane);

    public abstract void updateAllImageViews();

    public abstract void draw(Pane pane, int actualX, int actualY);
}

