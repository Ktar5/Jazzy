package com.ktar5.mapeditor.tiles.tileset;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.Getter;
import org.mini2Dx.gdx.utils.IntMap;
import org.pmw.tinylog.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Getter
public class Tileset {
    private IntMap<Image> tileImages;
    private File sourceFile, tilesetFile;
    private int tileSize;
    private int paddingVertical, paddingHorizontal;
    private int offsetLeft, offsetUp;

    public Tileset(File sourceFile, File tilesetFile, int tileSize, int paddingVertical, int paddingHorizontal,
                   int offsetLeft, int offsetUp) {
        this.sourceFile = sourceFile;
        this.tilesetFile = tilesetFile;
        this.tileSize = tileSize;
        this.offsetLeft = offsetLeft;
        this.offsetUp = offsetUp;
        this.paddingHorizontal = paddingHorizontal;
        this.paddingVertical = paddingVertical;
        tileImages = new IntMap<>();
        try {
            getTilesetImages(ImageIO.read(sourceFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getTilesetImages(BufferedImage image) {
        int index = 0;

        int columns = (image.getWidth() - offsetLeft + paddingHorizontal) / (tileSize + paddingHorizontal);
        //System.out.println("Columns: " + columns);

        int rows = (image.getHeight() - offsetUp + paddingVertical) / (tileSize + paddingVertical);
        //System.out.println("Rows: " + rows);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                BufferedImage subImage;
                try {
                    subImage = image.getSubimage(
                            offsetLeft + ((paddingHorizontal + tileSize) * col),
                            offsetUp + ((paddingVertical + tileSize) * row),
                            tileSize, tileSize);
                } catch (RasterFormatException e) {
                    Logger.info("Reached end of row");
                    break;
                }
                tileImages.put(index++, SwingFXUtils.toFXImage(subImage, null));
            }
        }
    }

    public void save() {
        if (getTilesetFile().exists()) {
            getTilesetFile().delete();
        }

        try {
            getTilesetFile().createNewFile();
            FileWriter writer = new FileWriter(getTilesetFile());
            writer.write(TilesetSerializer.serialize(this).toString(4));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("done");
    }


}
