package com.ktar5.mapeditor;

import com.ktar5.mapeditor.input.KeyPress;
import com.ktar5.mapeditor.input.Scroll;
import com.ktar5.mapeditor.gui.Root;
import com.ktar5.mapeditor.tilemaps.MapManager;
import com.ktar5.mapeditor.tileset.BaseTileset;
import com.ktar5.mapeditor.tileset.TilesetDeserializer;
import com.ktar5.mapeditor.tileset.TilesetManager;
import com.ktar5.mapeditor.util.StringUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.json.JSONObject;

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
        // testSaveTileset();
        testLoadTileset();
    }

    public void testSaveTileset() {
        File imageFile = new File(".\\mapgen\\test2.png");
        File tilesetfile = new File(".\\mapgen\\test.json");
        BaseTileset baseTileset = new BaseTileset(imageFile, tilesetfile, 16, 2, 2, 2, 2);
        Canvas canvas = new Canvas(128, 128);
        for (int i = 0; i < baseTileset.getTileImages().size; i++) {
            canvas.getGraphicsContext2D().drawImage(baseTileset.getTileImages().get(i),
                    (i % 7) * (baseTileset.getTileSize() + 2), ((i) / 7) * (baseTileset.getTileSize() + 2));
        }
        root.getChildren().addAll(canvas);
        TilesetManager.get().saveTileset(baseTileset.getId());
    }

    public void testLoadTileset() {
        File loaderFile = new File(".\\mapgen\\test.json");
        String data = StringUtil.readFileAsString(loaderFile);
        if (data == null || data.isEmpty()) {
            return;
        }
        BaseTileset deserialize = TilesetDeserializer.deserialize(loaderFile, new JSONObject(data));
        Canvas canvas = new Canvas(128, 128);
        for (int i = 0; i < deserialize.getTileImages().size; i++) {
            canvas.getGraphicsContext2D().drawImage(deserialize.getTileImages().get(i),
                    (i % 7) * (deserialize.getTileSize() + 2), ((i) / 7) * (deserialize.getTileSize() + 2));
        }
        root.getChildren().addAll(canvas);
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
//        // set number of tileset when tilemap is created
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
