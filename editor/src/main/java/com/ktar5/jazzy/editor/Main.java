package com.ktar5.jazzy.editor;

import com.ktar5.jazzy.editor.gui.Root;
import com.ktar5.jazzy.editor.tilemap.MapManager;
import com.ktar5.jazzy.editor.tileset.TilesetManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Main extends Application {
    public static Window window;
    public static Root root;
    
    public static void main(String[] args) {
        new MapManager();
        new TilesetManager();
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
    }
    
}
