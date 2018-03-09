package com.ktar5.mapeditor.tiles;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TileBlock extends Tile {
    private int blockId;
    private int direction;

    public TileBlock(int blockId, int direction, int x, int y) {
        super(x, y);
        this.blockId = blockId;
        this.direction = direction;
    }

    @Override
    public boolean isFoursquare() {
        return false;
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
