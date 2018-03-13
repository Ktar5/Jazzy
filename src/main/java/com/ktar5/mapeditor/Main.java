package com.ktar5.mapeditor;

import com.ktar5.mapeditor.javafx.Root;
import com.ktar5.mapeditor.input.KeyPress;
import com.ktar5.mapeditor.input.Scroll;
import com.ktar5.mapeditor.tilemap.MapManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.UUID;

public class Main extends Application {
    //Use scene builder -> add content through creating tabs and rendering to canvas.
    //Do check to see if canvas rendering is needed to be buffered
    //Do cache images
    //Do separate into multiple types of grids


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
        root.getCenterView().getEditorViewPane().createTab(UUID.randomUUID());
        root.getCenterView().getEditorViewPane().createTab(UUID.randomUUID());
        root.getCenterView().getEditorViewPane().createTab(UUID.randomUUID());

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
    }


//    int n = MapManager.get().getCurrent().getTileSize();
//    canvas = new BufferedImage(MapManager.get().getCurrent().getWidth() * n,
//            MapManager.get().getCurrent().getHeight() * n, BufferedImage.TYPE_INT_RGB);
//    graphics = (Graphics2D) canvas.getGraphics();
//    private BufferedImage canvas;
//    private Graphics2D graphics;
//
//    public void run() {
//        update();
//        render();
//        draw();
//    }
//
//    /**
//     * Updates the map editor.
//     */
//    private void update() {
//        // update main state
//        mainState.update();
//
//        // set number of tiles when tilemap is created
//        if (mainState.isMapCreated()) {
//            mainState.setMapCreated(false);
//        }
//
//        Input.handleInput();
//    }
//
//    /**
//     * Draws everything to an offscreen buffer. This is
//     * to prevent flickering.
//     */
//    private void render() {
//        int n = MapManager.get().getCurrent().getTileSize();
//        graphics.setColor(Color.BLACK);
//        graphics.fillRect(0, 0, MapManager.get().getCurrent().getWidth() * n,
//                MapManager.get().getCurrent().getHeight() * n);
//        mainState.draw(graphics);
//    }
//
//    /**
//     * Draws everything to the screen.
//     */
//    private void draw() {
//        Graphics2D tempGraphics = (Graphics2D) graphics;
//        tempGraphics.drawImage(canvas, getLocation().x, getLocation().y, null);
//        tempGraphics.dispose();
//    }


}
