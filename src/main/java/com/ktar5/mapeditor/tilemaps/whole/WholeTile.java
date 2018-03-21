package com.ktar5.mapeditor.tilemaps.whole;

import com.ktar5.mapeditor.gui.PixelatedImageView;
import com.ktar5.mapeditor.tileset.Tile;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class WholeTile extends Tile<WholeTileset> {
    private PixelatedImageView imageView;

    private int blockId;
    private int direction;

    public WholeTile(int blockId, int direction, WholeTileset tileset) {
        super(tileset);
        this.blockId = blockId;
        this.direction = direction;
    }

    @Override
    public String serialize() {
        return toString();
    }

    @Override
    public String toString() {
        if (direction == 0) {
            return String.valueOf(blockId);
        } else {
            return blockId + "_" + direction;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WholeTile wholeTile = (WholeTile) o;
        return blockId == wholeTile.blockId &&
                direction == wholeTile.direction;
    }

    public void updateImageView() {
        if (this.imageView == null) {
            this.imageView = new PixelatedImageView(getTileset().getTileImages().get(blockId));
        } else {
            this.imageView.setImage(getTileset().getTileImages().get(blockId));
        }
        this.imageView.setRotate(90 * direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockId, direction);
    }

    @Override
    public void remove(Pane pane) {
        if (imageView != null) {
            pane.getChildren().remove(imageView);
        }
        imageView = null;
    }

    @Override
    public void draw(Pane pane, int actualX, int actualY) {
        if (imageView == null) {
            updateImageView();
        }
        imageView.setTranslateX(actualX);
        imageView.setTranslateY(actualY);
        pane.getChildren().add(imageView);
        imageView.setVisible(true);
    }
}
