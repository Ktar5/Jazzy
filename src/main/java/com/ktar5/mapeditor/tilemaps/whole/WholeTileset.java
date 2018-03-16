package com.ktar5.mapeditor.tilemaps.whole;

import com.ktar5.mapeditor.tileset.BaseTileset;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;

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
                //final WritableImage writableImage = SwingFXUtils.toFXImage(subImage, null);
                this.getTileImages().put(index++, subImage);
            }
        }
    }

    @Override
    public void draw(Graphics graphics) {
        for (int i = 0; i < this.getTileImages().size; i++) {
            graphics.drawImage(this.getTileImages().get(i),
                    ((i % 7) * (this.getTileSize() + 2)) + 1,
                    (((i) / 7) * (this.getTileSize() + 2)) + 1,
                    null);
            /*getCanvas().getGraphicsContext2D().drawImage(scale(this.getTileImages().get(i)),
                    ((i % 7) * (this.getTileSize() + 2)), (((i) / 7) * (this.getTileSize() + 2)));
                    */
        }
    }

    public Image scale(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        IntBuffer src = IntBuffer.allocate(width * height);
        WritablePixelFormat<IntBuffer> pf = PixelFormat.getIntArgbInstance();
        image.getPixelReader().getPixels(0, 0, width, height, pf, src, width);
        int newWidth = width * SCALE;
        int newHeight = height * SCALE;
        int[] dst = new int[newWidth * newHeight];
        int index = 0;
        for (int y = 0; y < height; y++) {
            index = y * newWidth * SCALE;
            for (int x = 0; x < width; x++) {
                int pixel = src.get();
                for (int i = 0; i < SCALE; i++) {
                    for (int j = 0; j < SCALE; j++) {
                        dst[index + i + (newWidth * j)] = pixel;
                    }
                }
                index += SCALE;
            }
        }
        WritableImage bigImage = new WritableImage(newWidth, newHeight);
        bigImage.getPixelWriter().setPixels(0, 0, newWidth, newHeight, pf, dst, 0, newWidth);
        //preview.setImage(bigImage);
        //preview.setFitWidth(newWidth);
        return bigImage;
    }


}
