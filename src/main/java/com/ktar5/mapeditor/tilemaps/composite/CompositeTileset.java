package com.ktar5.mapeditor.tilemaps.composite;

import com.ktar5.mapeditor.tileset.BaseTileset;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class CompositeTileset extends BaseTileset {
    public CompositeTileset(File sourceFile, File tilesetFile, int tileSize, int paddingVertical, int paddingHorizontal, int offsetLeft, int offsetUp) {
        super(sourceFile, tilesetFile, tileSize, paddingVertical, paddingHorizontal, offsetLeft, offsetUp);
    }

    public CompositeTileset(File tilesetFile, JSONObject json) {
        super(tilesetFile, json);
    }

    @Override
    public void getTilesetImages(BufferedImage image) {
        //TODO
    }

    @Override
    public void draw(Graphics g) {

    }

}
