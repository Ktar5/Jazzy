package com.ktar5.mapeditor.tilemaps.sided;

import com.ktar5.mapeditor.tileset.Tile;
import javafx.scene.layout.Pane;

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
            String[] numbers = parts[i].split("_");
            if (numbers.length == 1) {
                //do nothing-- already = 0
            } else if (numbers.length == 2) {
                //side value 0, but corner needs setting
                setIdAndData(side.ordinal(), Integer.valueOf(numbers[0]), Data.values()[Integer.valueOf(numbers[1])]);
            } else if (numbers.length == 3) {
                side.setValue(this, Integer.valueOf(numbers[0]));
                setIdAndData(side.ordinal(), Integer.valueOf(numbers[1]), Data.values()[Integer.valueOf(numbers[2])]);
            }
        }
    }

    public void setSide(Side side, int baseId) {
        if (side.getValue(this) == baseId) {
            return;
        }
        side.setValue(this, baseId);
        refreshSide(side);
    }

    private void setIdAndData(int corner, int id, Data data) {
        tileparts[corner].setData(data);
        tileparts[corner].setBaseId(id);
        tileparts[corner].updateImageView(getTileset());
    }

    //done!!
    public void refreshSide(Side side) {
        if (side.getValue(this) != 0) {
            //right
            if (side.next().getValue(this) == 0) {
                //set the right corner of side to a straight piece
                setIdAndData(side.rightCorner(), side.getValue(this), Data.values()[3 + side.rightCorner()]);
            } else if (side.getValue(this) == side.next().getValue(this)) {
                //set the right corner of a side to a outer corner piece
                setIdAndData(side.rightCorner(), side.getValue(this), Data.OUTER_CORNER);
            }//else unallowed

            //left
            if (side.previous().getValue(this) == 0) {
                //set the left corner of side to a straight piece
                setIdAndData(side.leftCorner(), side.getValue(this), Data.values()[3 + side.ordinal()]);
            } else if (side.getValue(this) == side.previous().getValue(this)) {
                //set the right corner of a side to a outer corner piece
                setIdAndData(side.leftCorner(), side.getValue(this), Data.OUTER_CORNER);
            }//else unallowed
        } else {
            //right
            if (side.next().getValue(this) != 0) {
                setIdAndData(side.rightCorner(), side.next().getValue(this), Data.values()[3 + side.next().ordinal()]);
            } else {
                setIdAndData(side.rightCorner(), 0, Data.NONE);
            }

            //left
            if (side.previous().getValue(this) != 0) {
                setIdAndData(side.leftCorner(), side.previous().getValue(this), Data.values()[3 + side.previous().ordinal()]);
            } else {
                setIdAndData(side.leftCorner(), 0, Data.NONE);
            }
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
        for (Side side : Side.values()) {
            /*if (part != null && part.getImageView() != null) {
                pane.getChildren().remove(part.getImageView());
                part.setImageView(null);
            }*/
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
            if (part.getImageView() == null) {
                return;
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
        for (Side side : Side.values()) {
            if (side.getValue(this) != 0) {
                builder.append(side.getValue(this));
                builder.append("_");
            }
            builder.append(tileparts[side.ordinal()].serialize());
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

        public static Side fromRemainders(double xRemainder, double yRemainder) {
            System.out.println();
            System.out.println(xRemainder + "  ---  " + yRemainder);
            if (xRemainder > .8) {
                System.out.println("right");
                return RIGHT;
            } else if (xRemainder < .2) {
                System.out.println("left");
                return LEFT;
            } else if (yRemainder > .8) {
                System.out.println("up");
                return UP;
            } else {
                System.out.println("down");
                return DOWN;
            }
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
            return confine(this.ordinal() + 1);
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
