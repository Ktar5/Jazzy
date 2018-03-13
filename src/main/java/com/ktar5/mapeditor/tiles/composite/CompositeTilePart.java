package com.ktar5.mapeditor.tiles.composite;

import com.ktar5.mapeditor.util.ToolSerializeable;
import lombok.Getter;

import java.util.Objects;

@Getter
public class CompositeTilePart implements ToolSerializeable {
    private int baseId;
    private int data;

    public CompositeTilePart(int baseId, int data) {
        this.baseId = baseId;
        this.data = data;
    }

    public CompositeTilePart(String s) {
        if (s.length() == 1 || s.charAt(0) == '0') {
            baseId = 0;
            data = 0;
        } else if (s.contains("_")) {
            String[] split = s.split("_");
            baseId = Integer.valueOf(split[0]);
            data = Integer.valueOf(split[1]);
        }
    }

    void setData(int data) {
        this.data = data;
    }

    void setData(TilepartData tilepartData) {
        this.data = tilepartData.ordinal();
    }

    @Override
    public String serialize() {
        if (baseId == 0) {
            return "0";
        }
        return baseId + "_" + data;
    }

    public enum TilepartData {
        INNER_CORNER,
        OUTER_CORNER,
        NORTH_FACE,
        EAST_FACE,
        SOUTH_FACE,
        WEST_FACE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeTilePart that = (CompositeTilePart) o;
        return baseId == that.baseId &&
                data == that.data;
    }

    @Override
    public int hashCode() {

        return Objects.hash(baseId, data);
    }
}
