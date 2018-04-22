package com.ktar5.jazzy.tilemaps.sided;

import com.ktar5.jazzy.tileset.Tile;
import javafx.scene.layout.Pane;
import lombok.AllArgsConstructor;

import java.util.Arrays;

public class SidedTile extends Tile<SidedTileset> {
    //[3,0]
    //[2,1]
    private SidedTilePart[] tileparts = new SidedTilePart[4];
    private int upSide = 0, rightSide = 0, downSide = 0, leftSide = 0;
    
    
    public SidedTile(SidedTileset tileset) {
        super(tileset);
        for (int i = 0; i < tileparts.length; i++) {
            tileparts[i] = new SidedTilePart(0, Data.NONE);
        }
    }
    
    public SidedTile(SidedTileset tileset, String block) {
        this(tileset);
        block = block.substring(1, block.length() - 1);
        String[] parts = block.split("/");
        for (int i = 0; i < 4; i++) {
            Side side = Side.values()[i];
            if (!parts[i].equals("0")) {
                setSide(side, Integer.valueOf(parts[i]));
            }
            /*Side side = Side.values()[i];
            String[] numbers = parts[i].split("_");
            if (numbers.length == 1) {
                //do nothing-- already = 0
            } else if (numbers.length == 2) {
                //side value 0, but corner needs setting
                setIdAndData(side.ordinal(), Integer.valueOf(numbers[0]), Data.values()[Integer.valueOf(numbers[1])]);
            } else if (numbers.length == 3) {
                side.setValue(this, Integer.valueOf(numbers[0]));
                setIdAndData(side.ordinal(), Integer.valueOf(numbers[1]), Data.values()[Integer.valueOf(numbers[2])]);
            }*/
        }
    }
    
    public void setSide(Side side, int baseId) {
        if (baseId == 0) {
            throw new RuntimeException("Use remove instead");
        }
        if (side.getValue(this) == baseId) {
            System.out.println("Is same");
            return;
        }
        side.setValue(this, baseId);
        refreshSide(side);
    }
    
    private void updateCorner(int corner, int id, Data data) {
        if (data.ordinal() == 0) throw new RuntimeException("Don't set equal to 0");
        if (data.ordinal() >= Data.UP_SIDE.ordinal()) throw new RuntimeException("Don't use updateCorner for sides");
        if (getTileset() == null) return;
        tileparts[corner].setData(data);
        tileparts[corner].setBaseId(id);
        System.out.println("Image view isnt null. ID: " + (((id - 1) * 3) + data.ordinal() - 1));
        tileparts[corner].getImageView().setImage(getTileset().getTileImages().get(((id - 1) * 3) + data.ordinal() - 1));
        tileparts[corner].getImageView().setRotate(90 * corner);
    }
    
    private void updateFlat(int corner, int id, Data data, Side facing) {
        if (data.ordinal() == 0) throw new RuntimeException("Don't set equal to 0");
        if (data.ordinal() < Data.UP_SIDE.ordinal()) throw new RuntimeException("Don't use updateFlat for flat pieces");
        if (getTileset() == null) return;
        
        tileparts[corner].setData(data);
        tileparts[corner].setBaseId(id);
        int i = data.ordinal();
        if (i >= 3) i = 3;
        System.out.println("Image view isnt null. ID: " + (((id - 1) * 3) + i - 1));
        tileparts[corner].getImageView().setImage(getTileset().getTileImages().get(((id - 1) * 3) + i - 1));
        tileparts[corner].getImageView().setRotate(facing.ordinal() * 90);
    }
    
    //done!!
    public void refreshSide(Side side) {
        if (side.getValue(this) != 0) {
            //right
            if (side.next().getValue(this) == 0) {
                //flat: set the right corner of side to a straight piece
                System.out.println("Right: " + (3 + side.rightCorner()));
                updateFlat(side.rightCorner(), side.getValue(this), Data.values()[3 + side.rightCorner()], side);
            } else if (side.getValue(this) == side.next().getValue(this)) {
                //corner: set the right corner of a side to a outer corner piece
                updateCorner(side.rightCorner(), side.getValue(this), Data.OUTER_CORNER);
            }//else unallowed
            
            //left
            if (side.previous().getValue(this) == 0) {
                //flat: set the left corner of side to a straight piece
                System.out.println("Left: " + (3 + side.ordinal()));
                updateFlat(side.leftCorner(), side.getValue(this), Data.values()[3 + side.ordinal()], side);
            } else if (side.getValue(this) == side.previous().getValue(this)) {
                //corner: set the right corner of a side to a outer corner piece
                updateCorner(side.leftCorner(), side.getValue(this), Data.OUTER_CORNER);
            }//else unallowed
        }
    }
    
    @Override
    public void remove(Pane pane) {
        for (Side side : Side.values()) {
            removeSide(side);
        }
    }
    
    @Override
    public void updateAllImageViews() {
        throw new RuntimeException("Not implemented");
    }
    
    public void removeSide(Side side) {
        side.setValue(this, 0);
        
        //right
        if (side.next().getValue(this) != 0) {
            updateFlat(side.rightCorner(), side.next().getValue(this), Data.values()[3 + side.next().ordinal()], side.next());
        } else {
            tileparts[side.rightCorner()].getImageView().setImage(null);
            tileparts[side.rightCorner()].setBaseId(0);
            tileparts[side.rightCorner()].setData(Data.NONE);
        }
        
        //left
        if (side.previous().getValue(this) != 0) {
            updateFlat(side.leftCorner(), side.previous().getValue(this), Data.values()[3 + side.previous().ordinal()], side.previous());
        } else {
            tileparts[side.leftCorner()].getImageView().setImage(null);
            tileparts[side.leftCorner()].setBaseId(0);
            tileparts[side.leftCorner()].setData(Data.NONE);
        }
    }
    
    @Override
    public void draw(Pane pane, int actualX, int actualY) {
        if (getTileset() == null) {
            return;
        }
        for (Side side : Side.values()) {
            SidedTilePart part = tileparts[side.ordinal()];
            Corner corner = Corner.values()[side.ordinal()];
            if (part == null) {
                continue;
            }
            if (pane.getChildren().contains(part.getImageView())) {
                System.out.println("Already contained it");
                return;
            }
            pane.getChildren().add(part.getImageView());
            part.getImageView().setTranslateX(actualX + (corner.x * this.getTileset().getTileWidth()));
            part.getImageView().setTranslateY(actualY + (corner.y * this.getTileset().getTileHeight()));
            part.getImageView().setVisible(true);
        }
    }
    
    @Override
    public String serialize() {
        StringBuilder builder = new StringBuilder("[");
        for (Side side : Side.values()) {
            /*if (side.getValue(this) != 0) {
                builder.append(side.getValue(this));
                builder.append("_");
            }
            builder.append(tileparts[side.ordinal()].serialize());
            */
            builder.append(side.getValue(this));
            builder.append("/");
            
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");
        if (builder.toString().equals("[0/0/0/0]")) {
            return "0";
        }
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
    
    @AllArgsConstructor
    public enum Corner {
        UR(1, 0), DR(1, 1), DL(0, 1), UL(0, 0);
        public final double x, y;
    }
    
    @AllArgsConstructor
    public enum Side {
        UP(0, 0), RIGHT(.25, .25), DOWN(0, .5), LEFT(-.25, .25);
        
        public final double x, y;
        
        public static Side fromRemainders(double xRemainder, double yRemainder) {
            int angle = (int) Math.toDegrees(Math.atan2(yRemainder - .5, xRemainder - .5));
            angle += 360 + 90 + 45;
            angle = angle % 360;
            return Side.values()[angle / 90];
        }
        
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
        
        public void setValue(SidedTile tile, int value) {
            switch (this) {
                case UP:
                    tile.upSide = value;
                    break;
                case RIGHT:
                    tile.rightSide = value;
                    break;
                case DOWN:
                    tile.downSide = value;
                    break;
                case LEFT:
                    tile.leftSide = value;
                    break;
            }
        }
        
        public int leftCorner() {
            return confine(this.ordinal() - 1);
        }
        
        public int rightCorner() {
            return this.ordinal();
        }
        
        public Side next() {
            return Side.values()[confine(this.ordinal() + 1)];
        }
        
        public Side previous() {
            return Side.values()[confine(this.ordinal() - 1)];
        }
        
        private int confine(int n) {
            if (n < 0) return 3;
            else if (n > 3) return 0;
            else return n;
        }
    }
    
    public enum Data {
        NONE,
        INNER_CORNER,
        OUTER_CORNER,
        UP_SIDE,
        RIGHT_SIDE,
        DOWN_SIDE,
        LEFT_SIDE
    }
    
}
