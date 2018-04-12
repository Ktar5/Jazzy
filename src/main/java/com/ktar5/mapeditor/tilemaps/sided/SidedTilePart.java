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
    private SidedTile.Data data;
    //0 = none
    //1 = outer corner
    //2 = corner
    //3-6 = sides
    
    public SidedTilePart(int baseId, SidedTile.Data data) {
        this.baseId = baseId;
        this.data = data;
    }
    
    public void updateImageView(SidedTileset tileset) {
        if (tileset == null) {
            return;
        }
        if (this.imageView == null) {
            int i = data.ordinal();
            if (i == 0) return;
            if (i > 3) i -= 3;
            System.out.println((baseId * 3) + i);
            this.imageView = new PixelatedImageView(tileset.getTileImages().get((baseId * 3) + i));
            if (data.ordinal() > SidedTile.Data.UP_SIDE.ordinal()) {
                imageView.setRotate(i * 90);
            }
        } else {
            int i = data.ordinal();
            if (i == 0) return;
            if (i > 3) i -= 3;
            System.out.println((baseId * 3) + i);
            this.imageView.setImage(tileset.getTileImages().get((baseId * 3) + i));
            if (data.ordinal() > SidedTile.Data.UP_SIDE.ordinal()) {
                imageView.setRotate(i * 90);
            }
        }
    }
    
    @Override
    public String serialize() {
        if (baseId == 0) {
            return "0";
        }
        return baseId + "_" + data.ordinal();
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
