package com.ktar5.mapeditor;

import com.ktar5.mapeditor.gui.Root;
import com.ktar5.mapeditor.input.KeyPress;
import com.ktar5.mapeditor.input.Scroll;
import com.ktar5.mapeditor.tilemaps.MapManager;
import com.ktar5.mapeditor.tilemaps.whole.WholeTileset;
import com.ktar5.mapeditor.tileset.BaseTileset;
import com.ktar5.mapeditor.tileset.TilesetManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

public class Main extends Application {
    //Multiple types of grids
    //Multiple types of tileset

    public static Window window;
    public static Root root;

    public static void main(String[] args) {
        File file = new File("D:\\GameDev\\Projects\\TileJumpGame\\tools\\mapgen");
        if (!file.exists()) {
            file.mkdir();
        }
        new MapManager(file);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TileMapTest");

        //this makes all stages close and the app exit when the main stage is closed
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        //Create root pane
        root = new Root();

        //Initialize primary stage window and set to view scene
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        primaryStage.setMinWidth(350);
        primaryStage.setMinHeight(150);
        primaryStage.show();

        //Initialize window
        window = scene.getWindow();

        //Add event listeners
        scene.setOnKeyPressed(new KeyPress());
        scene.setOnScroll(new Scroll());

        //TESTS
        //testSaveTileset();
        testLoadTileset();
    }

    public void testSaveTileset() {
        WholeTileset tileset = TilesetManager.get().createTileset();
        TilesetManager.get().saveTileset(tileset.getId());
        TilesetManager.get().remove(tileset.getId());
    }

    public void testLoadTileset() {
        BaseTileset baseTileset = TilesetManager.get().loadTileset();
    }

}
