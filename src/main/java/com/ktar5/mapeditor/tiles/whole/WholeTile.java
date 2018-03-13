package com.ktar5.mapeditor.tiles.whole;

import com.ktar5.mapeditor.tiles.Tile;
import javafx.scene.canvas.Canvas;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WholeTile extends Tile {
    private int blockId;
    private int direction;

    public WholeTile(int blockId, int direction) {
        this.blockId = blockId;
        this.direction = direction;
    }

    @Override
    public boolean isFoursquare() {
        return false;
    }

    @Override
    public void draw(Canvas canvas, int x, int y) {

    }

    @Override
    public String serialize() {
        return String.valueOf(blockId);
    }

    @Override
    public String toString() {
        return blockId + "_" + direction;
    }
}
