package com.ktar5.mapeditor.tiles.tileset;

import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TilesetDeserializer {

    public static Tileset deserialize(File tilesetFile, JSONObject json) {
        Path path = Paths.get(tilesetFile.getPath()).resolve(json.getString("sourceFile"));
        File sourceFile = path.toFile();
        return new Tileset(
                sourceFile,
                tilesetFile,
                json.getInt("tileSize"),
                json.getJSONObject("padding").getInt("vertical"),
                json.getJSONObject("padding").getInt("horizontal"),
                json.getJSONObject("offset").getInt("left"),
                json.getJSONObject("offset").getInt("up"));
    }
}
