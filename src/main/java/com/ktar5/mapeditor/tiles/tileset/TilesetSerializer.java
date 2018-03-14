package com.ktar5.mapeditor.tiles.tileset;

import org.json.JSONObject;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TilesetSerializer {

    public JSONObject serialize(Tileset src) {
        JSONObject json = new JSONObject();

        JSONObject padding = new JSONObject();
        padding.put("horizontal", src.getPaddingHorizontal());
        padding.put("vertical", src.getPaddingVertical());
        json.put("padding", padding);

        JSONObject offset = new JSONObject();
        offset.put("left", src.getOffsetLeft());
        offset.put("up", src.getOffsetUp());
        json.put("offset", offset);

        json.put("tileSize", src.getTileSize());

        Path path = Paths.get(src.getTilesetFile().getPath())
                .relativize(Paths.get(src.getSourceFile().getPath()));
        json.put("sourceFile", path.toString());
        return json;
    }
}
