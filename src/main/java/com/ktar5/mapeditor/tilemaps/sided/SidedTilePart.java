package com.ktar5.mapeditor.tilemaps.sided;

import com.ktar5.mapeditor.gui.utils.PixelatedImageView;
import com.ktar5.mapeditor.util.ToolSerializeable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter(AccessLevel.PACKAGE)
public class SidedTilePart implements ToolSerializeable {
    private PixelatedImageView imageView;
    
    private int baseId;
    private int data;
    
    public SidedTilePart(int baseId, int data) {
        this.baseId = baseId;
        this.data = data;
    }
    
    public SidedTilePart(String s) {
        if (s.length() == 1 || s.charAt(0) == '0') {
            baseId = 0;
            data = 0;
        } else if (s.contains("_")) {
            String[] split = s.split("_");
            baseId = Integer.valueOf(split[0]);
            data = Integer.valueOf(split[1]);
        }
    }
    
    public void updateImageView(SidedTileset tileset) {
        if (tileset == null) {
            return;
        }
        if (this.imageView == null) {
            if (baseId == 0) {
                this.imageView = new PixelatedImageView(null);
            } else {
                this.imageView = new PixelatedImageView(data);
            }
        } else {
            this.imageView.setImage(data);
        }
        this.imageView.setRotate(90 * (data % 2));
        //TODO need to make sided tileset work
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
        SidedTilePart that = (SidedTilePart) o;
        return baseId == that.baseId &&
                data == that.data;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(baseId, data);
    }
}
