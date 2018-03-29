package com.ktar5.mapeditor.tilemaps.composite;

import com.ktar5.mapeditor.gui.PixelatedImageView;
import com.ktar5.mapeditor.tileset.BaseTileset;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.File;

public class CompositeTileset extends BaseTileset {
    public CompositeTileset(File tilesetFile, JSONObject json) {
        super(tilesetFile, json);
    }

    public CompositeTileset(File sourceFile, File tilesetFile, int tileSize, int paddingVertical, int paddingHorizontal,
                            int offsetLeft, int offsetUp) {
        super(sourceFile, tilesetFile, tileSize, paddingVertical, paddingHorizontal, offsetLeft, offsetUp);
    }

    @Override
    public void getTilesetImages(BufferedImage image) {
        int index = 0;

        int columns = (image.getWidth() - getOffsetLeft()) / (getTileSize() + getPaddingHorizontal());
        int rows = (image.getHeight() - getOffsetUp()) / (getTileSize() + getPaddingVertical());

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                BufferedImage subImage = image.getSubimage(
                        getOffsetLeft() + ((getPaddingHorizontal() + getTileSize()) * col),
                        getOffsetUp() + ((getPaddingVertical() + getTileSize()) * row),
                        getTileSize(), getTileSize());
                subImage = scale(subImage, SCALE);
                final WritableImage writableImage = SwingFXUtils.toFXImage(subImage, null);
                this.getTileImages().put(index++, writableImage);
            }
        }
    }

    @Override
    public void onClick(MouseEvent event) {

    }

    @Override
    public void onDrag(MouseEvent event) {

    }

    @Override
    public void draw(Pane pane) {
        for (int i = 0; i < this.getTileImages().size; i++) {
            PixelatedImageView iv = new PixelatedImageView(this.getTileImages().get(0));
            iv.setVisible(true);
            iv.setTranslateX(((i % 7) * (this.getTileSize())));
            iv.setTranslateY((((i) / 7) * (this.getTileSize())));
            pane.getChildren().add(iv);
        }

    }

}
