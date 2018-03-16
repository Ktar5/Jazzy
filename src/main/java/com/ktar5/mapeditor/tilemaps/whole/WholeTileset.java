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

    private static final boolean useCanvas = true;

    @Override
    public void draw() {
        //pane.setTranslateY(-500);
        Pane pane = ((EditorTab) Main.root.getCenterView().getEditorViewPane().getSelectionModel().getSelectedItem()).getPane().getPane();
        for (int i = 0; i < this.getTileImages().size; i++) {
            PixelatedImageView iv = new PixelatedImageView(this.getTileImages().get(i));
            // assuming staggered if Y is odd move it right 1/2 a tile
            // also assuming 64x64 tile
            iv.setVisible(true);
            iv.setTranslateX(((i % 7) * (this.getTileSize() + 2)));
            iv.setTranslateY((((i) / 7) * (this.getTileSize() + 2)));
            pane.getChildren().add(iv);
            // at this point you might want to add the ImageView to a custom
            // "tile" class including your own info which you can then place
            // in a 2d array where the index's are the coordinates of the tile
            if (useCanvas) {
                System.out.println("draw");
                getCanvas().getGraphicsContext2D().drawImage(this.getTileImages().get(i),
                        ((i % 7) * (this.getTileSize() + 2)), (((i) / 7) * (this.getTileSize() + 2)));

            }
        }
        Main.root.getChildren().add(pane);
    }


}
