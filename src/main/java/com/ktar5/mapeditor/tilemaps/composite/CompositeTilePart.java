package com.ktar5.mapeditor.tilemaps.composite;

import com.ktar5.mapeditor.gui.PixelatedImageView;
import com.ktar5.mapeditor.util.ToolSerializeable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter(AccessLevel.PACKAGE)
public class CompositeTilePart implements ToolSerializeable {
    private PixelatedImageView imageView;

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

    public void updateImageView(CompositeTileset tileset) {
        if (this.imageView == null) {
            this.imageView = new PixelatedImageView(tileset.getTileImages().get(5));
        } else {
            this.imageView.setImage(tileset.getTileImages().get(5));
        }
        this.imageView.setRotate(90 * 5);
        //TODO need to make composite tileset work
    }

    @Override
    public String serialize() {
        if (baseId == 0) {
            return "0";
        }
        return baseId + "_" + data;
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
