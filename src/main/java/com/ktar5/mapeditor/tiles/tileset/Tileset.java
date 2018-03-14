package com.ktar5.mapeditor.tiles.tileset;

import javafx.scene.image.Image;
import lombok.Getter;
import org.mini2Dx.gdx.utils.IntMap;

import java.io.File;

@Getter
public class Tileset {
    private IntMap<Image> tileImages;
    private File sourceFile, tilesetFile;
    private int tileSize;
    private int paddingLeft, paddingRight, paddingUp, paddingDown;
    private int offsetLeft, offsetUp;

    public Tileset(File sourceFile, File tilesetFile, int tileSize, int paddingLeft, int paddingRight, int paddingUp, int paddingDown,
                   int offsetLeft, int offsetUp) {
        this.sourceFile = sourceFile;
        this.tilesetFile = tilesetFile;
        this.tileSize = tileSize;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        this.paddingUp = paddingUp;
        this.paddingDown = paddingDown;
        this.offsetLeft = offsetLeft;
        this.offsetUp = offsetUp;
    }

    public class TilesetDeserializer {
        public Tileset deserialize(Tileset src) {
            return null;
        }
    }


}
