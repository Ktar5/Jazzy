package com.ktar5.mapeditor.tiles;

public class TileFoursquare extends Tile {
    Tilepart[] tileparts = new Tilepart[4];

    public TileFoursquare(int x, int y) {
        super(x, y);
        for (int i = 0; i < tileparts.length; i++) {
            tileparts[i] = new Tilepart(0, 0);
        }
    }

    public TileFoursquare(int x, int y, String block) {
        this(x, y);
        block = block.substring(1, block.length());
        String[] split = block.split("/");
        for (int i = 0; i < split.length; i++) {
            tileparts[i] = new Tilepart(split[i]);
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
    public String toString() {
        return serialize();
    }
}
