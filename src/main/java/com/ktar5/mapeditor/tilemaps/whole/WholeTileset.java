package com.ktar5.mapeditor.tilemaps.whole;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.gui.PixelatedImageView;
import com.ktar5.mapeditor.tileset.BaseTileset;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
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

    public static class ImageTestView extends PixelatedImageView{

        WholeTileset tileset;
        int num;

        public ImageTestView(WholeTileset tileset, int num) {
            super(tileset.getTileImages().get(num));
            this.tileset = tileset;
            this.num = num;
        }

        public void incrementImage(){
            this.num++;
            if(num > tileset.getTileImages().size - 1){
                num = 0;
            }
            this.setImage(tileset.getTileImages().get(num));
        }
    }

    @Override
    public void draw() {
        Group pane = Main.root.getCenterView().getEditorViewPane().getTabDrawingPane(getId());
        for (int i = 0; i < this.getTileImages().size; i++) {
            PixelatedImageView iv = new ImageTestView(this, i);
            iv.setVisible(true);
            iv.setTranslateX(((i % 7) * (this.getTileSize() + 2)));
            iv.setTranslateY((((i) / 7) * (this.getTileSize() + 2)));
            pane.getChildren().add(iv);
        }

    }


}
