package com.ktar5.mapeditor.tileset;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.Getter;
import org.json.JSONObject;
import org.mini2Dx.gdx.utils.IntMap;
import org.pmw.tinylog.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Getter
public class BaseTileset {
    private UUID id;
    private IntMap<Image> tileImages;
    private File sourceFile, tilesetFile;
    private int tileSize;
    private int paddingVertical, paddingHorizontal;
    private int offsetLeft, offsetUp;

    public BaseTileset(File sourceFile, File tilesetFile, int tileSize, int paddingVertical, int paddingHorizontal,
                       int offsetLeft, int offsetUp) {
        this.sourceFile = sourceFile;
        this.tilesetFile = tilesetFile;
        this.tileSize = tileSize;
        this.offsetLeft = offsetLeft;
        this.offsetUp = offsetUp;
        this.paddingHorizontal = paddingHorizontal;
        this.paddingVertical = paddingVertical;
        tileImages = new IntMap<>();
        this.id = UUID.randomUUID();
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

    public JSONObject serialize() {
        JSONObject json = new JSONObject();

        JSONObject padding = new JSONObject();
        padding.put("horizontal", this.getPaddingHorizontal());
        padding.put("vertical", this.getPaddingVertical());
        json.put("padding", padding);

        JSONObject offset = new JSONObject();
        offset.put("left", this.getOffsetLeft());
        offset.put("up", this.getOffsetUp());
        json.put("offset", offset);

        json.put("tileSize", this.getTileSize());

        Path path = Paths.get(this.getTilesetFile().getPath())
                .relativize(Paths.get(this.getSourceFile().getPath()));
        json.put("sourceFile", path.toString());
        return json;
    }


}
