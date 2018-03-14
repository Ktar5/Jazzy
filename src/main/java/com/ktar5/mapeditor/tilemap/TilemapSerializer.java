package com.ktar5.mapeditor.tilemap;

import org.json.JSONObject;

public class TilemapSerializer {

    public static JSONObject serialize(Tilemap src) {
        JSONObject json = new JSONObject();

        JSONObject dimensions = new JSONObject();
        dimensions.put("width", src.getWidth());
        dimensions.put("height", src.getHeight());
        json.put("dimensions", dimensions);

        JSONObject spawn = new JSONObject();
        spawn.put("x", src.getXStart());
        spawn.put("y", src.getYStart());
        json.put("spawn", spawn);

        json.put("tileSize", src.getTileSize());

        json.put("tilemap", src.getJsonArray());
        return json;
    }
}
