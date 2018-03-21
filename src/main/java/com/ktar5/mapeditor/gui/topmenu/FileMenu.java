package com.ktar5.mapeditor.gui.topmenu;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tilemaps.MapManager;
import com.ktar5.mapeditor.tileset.BaseTileset;
import com.ktar5.mapeditor.tileset.TilesetManager;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class FileMenu extends Menu {

    public FileMenu() {
        super("File");
        final MenuItem openTilemap = new MenuItem("Open Tilemap");
        openTilemap.setOnAction(event -> {
            final BaseTilemap baseTilemap = MapManager.get().loadMap();
            if (baseTilemap != null) {
                Main.root.getCenterView().getEditorViewPane().setSelectedTab(baseTilemap.getId());
            }
        });

        final MenuItem openTileset = new MenuItem("Open Tileset");
        openTileset.setOnAction(event -> {
            final BaseTileset baseTileset = TilesetManager.get().loadTileset();
            if (baseTileset != null) {
                Main.root.getCenterView().getEditorViewPane().setSelectedTab(baseTileset.getId());
            }
        });

        final MenuItem newMap = new MenuItem("New Tilemap");
        newMap.setOnAction(event -> MapManager.get().createMap());

        final MenuItem newTileset = new MenuItem("New Tileset");
        newTileset.setOnAction(event -> TilesetManager.get().createTileset());

        final MenuItem save = new MenuItem("Save Current");
        save.setOnAction(event -> Main.root.getCurrentTab().getTabbable().save());

        final MenuItem saveAs = new MenuItem("Save As..");
        saveAs.setOnAction(event -> Main.root.getCurrentTab().getTabbable().saveAs());

        this.getItems().addAll(
                newMap,
                openTilemap,
                newTileset,
                openTileset,
                new MenuItem("Open Recent"),
                save,
                saveAs,
                new MenuItem("Revert")
        );
    }

}
