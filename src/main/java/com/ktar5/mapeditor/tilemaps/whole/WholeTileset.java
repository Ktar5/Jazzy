package com.ktar5.mapeditor.tilemaps.whole;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.gui.PixelatedImageView;
import com.ktar5.mapeditor.gui.centerview.editor.tabs.EditorTab;
import com.ktar5.mapeditor.tileset.BaseTileset;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.File;

public class WholeTileset extends BaseTileset {

    public WholeTileset(File tilesetFile, JSONObject json) {
        super(tilesetFile, json);
    }

    public WholeTileset(File sourceFile, File tilesetFile, int tileSize, int paddingVertical, int paddingHorizontal,
                        int offsetLeft, int offsetUp) {
        super(sourceFile, tilesetFile, tileSize, paddingVertical, paddingHorizontal, offsetLeft, offsetUp);
    }

    @Override
    public void getTilesetImages(BufferedImage image) {
        int index = 0;

        int columns = (image.getWidth() - getOffsetLeft() + getPaddingHorizontal())
                / (getTileSize() + getPaddingHorizontal());
        int rows = (image.getHeight() - getOffsetUp() + getPaddingVertical())
                / (getTileSize() + getPaddingVertical());

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
    public void draw() {
        Pane pane = Main.root.getCenterView().getEditorViewPane().getTabDrawingPane(getId());
        for (int i = 0; i < this.getTileImages().size; i++) {
            PixelatedImageView iv = new PixelatedImageView(this.getTileImages().get(i));
            iv.setVisible(true);
            iv.setTranslateX(((i % 7) * (this.getTileSize() + 2)));
            iv.setTranslateY((((i) / 7) * (this.getTileSize() + 2)));
            pane.getChildren().add(iv);
        }
    }


}
