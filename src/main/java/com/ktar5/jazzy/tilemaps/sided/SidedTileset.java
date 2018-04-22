package com.ktar5.jazzy.tilemaps.sided;

import com.ktar5.jazzy.gui.utils.TilesetImageView;
import com.ktar5.jazzy.tileset.BaseTileset;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.File;

public class SidedTileset extends BaseTileset {
    public SidedTileset(File tilesetFile, JSONObject json) {
        super(tilesetFile, json);
    }
    
    public SidedTileset(File sourceFile, File saveFile, int paddingVertical, int paddingHorizontal, int offsetLeft, int offsetUp, int tileWidth, int tileHeight) {
        super(sourceFile, saveFile, paddingVertical, paddingHorizontal, offsetLeft, offsetUp, tileWidth, tileHeight);
    }
    
    @Override
    public void getTilesetImages(BufferedImage image) {
        int index = 0;
        
        int columns = (image.getWidth() - getOffsetLeft()) / (getTileWidth() + getPaddingHorizontal());
        int rows = (image.getHeight() - getOffsetUp()) / (getTileHeight() + getPaddingVertical());
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                BufferedImage subImage = image.getSubimage(
                        getOffsetLeft() + ((getPaddingHorizontal() + getTileWidth()) * col),
                        getOffsetUp() + ((getPaddingVertical() + getTileHeight()) * row),
                        getTileWidth(), getTileHeight());
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
    public void onMove(MouseEvent event) {
    
    }
    
    @Override
    public void draw(Pane pane) {
        for (int i = 0; i < this.getTileImages().size; i++) {
            TilesetImageView iv = new TilesetImageView(this, i);
            iv.setVisible(true);
            iv.setTranslateX(((i % getColumns()) * (this.getTileWidth())));
            iv.setTranslateY((((i) / getRows()) * (this.getTileHeight())));
            pane.getChildren().add(iv);
        }
        
    }
    
}
