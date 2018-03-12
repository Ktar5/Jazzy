package com.ktar5.mapeditor.tiles;

import javafx.scene.image.Image;
import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Stores the images and extra data associated with a tile. Only one
 * instance of TileData for a given id should exist.
 */
@Getter
public class TileData {
    private int id;
    private Image image;

    /**
     * Construct a tile.
     *
     * @param filePath The path to the file.
     * @param id       The id that will represent the tile when saved.
     */
    public TileData(final String filePath, final int id) {
        try {
            FileInputStream inputstream = new FileInputStream("C:\\images\\image.jpg");
            image = new Image(inputstream);
            inputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println("Bad file path: " + filePath);
            System.exit(0);
        }
        this.id = id;
    }

}
