package com.ktar5.mapeditor.display;

import com.ktar5.mapeditor.tiles.Tile;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.HashMap;

public class MapPane2 {
    Scene scene;

    //Stage has scenes.
    //Scenes have


    public void start(Stage primaryStage) {

        Group root = new Group();

        scene = new Scene(root, 640, 480);

        primaryStage.setScene(scene);
        primaryStage.show();

        // we how have a mainPane that we can hang stuff on and see it
        // in a window...

        // loading tmx and expanding it into various java structures
        // it also loads the tile map images
        try {
            map = mapReader.readMap("untitled.tmx");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // assume just the one layer
        // you could have different layers on different
        // javafx nodes sitting on top of each other...
        layer = (TileLayer) map.getLayer(0);
        if (layer == null) {
            System.out.println("can't get map layer");
            System.exit(-1);
        }

        int width = layer.getWidth();
        int height = layer.getHeight();

        Tile tile = null;
        int tid;

        // as libtiled provides awt images we must convert them to
        // javafx images but we don't want a new image for every
        // single tile on the map
        HashMap<Integer, Image> tileHash = new HashMap<Integer, Image>();
        Image tileImage = null;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tile = layer.getTileAt(x, y);
                tid = tile.getId();
                if (tileHash.containsKey(tid)) {
                    // if we have already used the image get it from the hashmap
                    tileImage = tileHash.get(tid);
                } else {
                    tileHash.put(tid, tileImage);
                }
                ImageView iv = new ImageView(tileImage);
                // assuming staggered if Y is odd move it right 1/2 a tile
                // also assuming 64x64 tile
                iv.setTranslateX((y % 2 == 0 ? 0 : 32) + x * 64);
                iv.setTranslateY(y * 16);
                mainPane.getChildren().add(iv);
                // at this point you might want to add the ImageView to a custom
                // "tile" class including your own info which you can then place
                // in a 2d array where the index's are the coordinates of the tile

            }
        }
    }

}
