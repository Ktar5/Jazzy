package com.ktar5.mapeditor.tilemaps.composite;

import com.ktar5.mapeditor.tileset.Tile;
import javafx.scene.layout.Pane;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class CompositeTile extends Tile<CompositeTileset> {
    private CompositeTilePart[] tileparts = new CompositeTilePart[4];

    public CompositeTile(CompositeTileset tileset) {
        super(tileset);
        for (int i = 0; i < tileparts.length; i++) {
            tileparts[i] = new CompositeTilePart(0, 0);
        }
    }

    public CompositeTile(CompositeTileset tileset, String block) {
        this(tileset);
        block = block.substring(1, block.length() - 1);
        String[] split = block.split("/");
        for (int i = 0; i < split.length; i++) {
            tileparts[i] = new CompositeTilePart(split[i]);
        }
    }

    void setData(Corner corner, int data) {
        set(corner, tileparts[corner.ordinal()].getBaseId(), data);
    }

    void setBaseId(Corner corner, int baseId) {
        set(corner, baseId, tileparts[corner.ordinal()].getData());
    }

    void set(Corner corner, int baseId, int data) {
        tileparts[corner.ordinal()].setBaseId(baseId);
        tileparts[corner.ordinal()].setData(data);
        tileparts[corner.ordinal()].updateImageView(getTileset());
    }

    public void remove(Pane pane, Corner corner) {
        CompositeTilePart part = getTileparts()[corner.ordinal()];
        if (part != null && part.getImageView() != null) {
            pane.getChildren().remove(part.getImageView());
            part.setImageView(null);
        }

    }

    public void updateImageView() {
        if (getTileset() == null) {
            return;
        }
        for (CompositeTilePart tilePart : tileparts) {
            tilePart.updateImageView(getTileset());
        }
    }

    @Override
    public void remove(Pane pane) {
        for (Corner corner : Corner.values()) {
            remove(pane, corner);
        }
    }

    @Override
    public void draw(Pane pane, int actualX, int actualY) {
        if (getTileset() == null) {
            return;
        }
        for (CompositeTilePart part : tileparts) {
            if (part == null) {
                continue;
            }
            if (part.getImageView() == null) {
                part.updateImageView(getTileset());
            }
            pane.getChildren().add(part.getImageView());
            part.getImageView().setTranslateX(actualX);
            part.getImageView().setTranslateX(actualY);
            part.getImageView().setVisible(true);
        }
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

    public enum CompositeTilePartData {
        INNER_CORNER,
        OUTER_CORNER,
        NORTH_FACE,
        UP_EDGE,
        RIGHT_EDGE,
        DOWN_EDGE,
        LEFT_EDGE
    }

    public enum Corner {
        UP_RIGHT,
        UP_LEFT,
        DOWN_RIGHT,
        DOWN_LEFT;

        public static Corner fromRemainders(double xRemainder, double yRemainder) {
            if (xRemainder > .5 && yRemainder > .5) {
                return UP_RIGHT;
            } else if (xRemainder > .5 && yRemainder < .5) {
                return DOWN_RIGHT;
            } else if (xRemainder < .5 && yRemainder < .5) {
                return DOWN_LEFT;
            } else {
                return UP_LEFT;
            }
        }

    }
}
