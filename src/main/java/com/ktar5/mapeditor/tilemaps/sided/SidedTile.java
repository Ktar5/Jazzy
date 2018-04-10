package com.ktar5.mapeditor.tilemaps.sided;

import com.ktar5.mapeditor.tileset.Tile;
import javafx.scene.layout.Pane;

import java.util.Arrays;

import static com.ktar5.mapeditor.tilemaps.sided.SidedTile.Corner.UP_RIGHT;
import static com.ktar5.mapeditor.tilemaps.sided.SidedTile.Data.CORNER;
import static com.ktar5.mapeditor.tilemaps.sided.SidedTile.Data.H_SIDE;

public class SidedTile extends Tile<SidedTileset> {
    //[3,0]
    //[2,1]
    private SidedTilePart[] tileparts = new SidedTilePart[4];
    private int upSide = 0, rightSide = 0, downSide = 0, leftSide = 0;
    
    
    public SidedTile(SidedTileset tileset) {
        super(tileset);
        for (int i = 0; i < tileparts.length; i++) {
            tileparts[i] = new SidedTilePart(0, 0);
        }
    }
    
    public SidedTile(SidedTileset tileset, String block) {
        this(tileset);
        block = block.substring(1, block.length() - 1);
        String[] split = block.split("/");
        upSide = Integer.valueOf(split[0]);
        rightSide = Integer.valueOf(split[1]);
        downSide = Integer.valueOf(split[2]);
        leftSide = Integer.valueOf(split[3]);
    }
    
    //[3,0]
    //[2,1]
    public void refreshTileParts() {
        for (Side side : Side.values()) {
            if (upSide != 0) {
                if (upSide == rightSide) {
                    set(UP_RIGHT, upSide, CORNER.ordinal());
                } else {
                    set(UP_RIGHT, upSide, H_SIDE.ordinal());
                }
            } else {
                //TODO remove();
                set(UP_RIGHT, 0, 0);
            }
        }
    }
    
    void setBaseId(Side side, int baseId) {
        set(side, baseId, tileparts[corner.ordinal()].getData());
    }
    
    void set(Side side, int baseId) {
        tileparts[corner.ordinal()].setBaseId(baseId);
        //TODO
        tileparts[corner.ordinal()].setData(calculateData(corner, baseId));
        tileparts[corner.ordinal()].updateImageView(getTileset());
    }
    
    public void remove(Pane pane, Corner corner) {
        SidedTilePart part = tileparts[corner.ordinal()];
        if (part != null && part.getImageView() != null) {
            pane.getChildren().remove(part.getImageView());
            part.setImageView(null);
        }
        
    }
    
    public void updateImageView() {
        if (getTileset() == null) {
            return;
        }
        for (SidedTilePart tilePart : tileparts) {
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
        for (SidedTilePart part : tileparts) {
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
        SidedTile that = (SidedTile) o;
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
    
    public enum Side {
        UP, RIGHT, DOWN, LEFT;
        
        public int getValue(SidedTile tile) {
            switch (this) {
                case UP:
                    return tile.upSide;
                case RIGHT:
                    return tile.rightSide;
                case DOWN:
                    return tile.downSide;
                case LEFT:
                    return tile.leftSide;
            }
            throw new RuntimeException("Stupid");
        }
        
        public Side next() {
            return Side.values()[(this.ordinal() + 1) % 3];
        }
        
        public Side previous() {
            return Side.values()[(this.ordinal() + -1) < 0 ? 3 : (this.ordinal() + -1)];
        }
    }
    
    public enum Data {
        CORNER,
        H_SIDE,
        V_SIDE
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
