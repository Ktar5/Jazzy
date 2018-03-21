package com.ktar5.mapeditor.tilemaps.composite;

import com.ktar5.mapeditor.tileset.Tile;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class CompositeTile extends Tile {
    private CompositeTilePart[] tileparts = new CompositeTilePart[4];

    public CompositeTile() {
        for (int i = 0; i < tileparts.length; i++) {
            tileparts[i] = new CompositeTilePart(0, 0);
        }
    }

    public CompositeTile(String block) {
        this();
        block = block.substring(1, block.length() - 1);
        String[] split = block.split("/");
        for (int i = 0; i < split.length; i++) {
            tileparts[i] = new CompositeTilePart(split[i]);
        }
    }

    @Override
    public boolean isFoursquare() {
        return true;
    }

    @Override
    public String serialize() {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < tileparts.length; i++) {
            builder.append(tileparts[i].serialize());
            builder.append("/");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeTile that = (CompositeTile) o;
        return Arrays.equals(tileparts, that.tileparts);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tileparts);
    }

    @Override
    public String toString() {
        return serialize();
    }
}
