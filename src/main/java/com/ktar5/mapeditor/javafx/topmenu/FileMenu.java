package com.ktar5.mapeditor.javafx.topmenu;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.javafx.centerview.EditorTab;
import com.ktar5.mapeditor.tilemap.MapManager;
import com.ktar5.mapeditor.tilemap.Tilemap;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;

public class FileMenu extends Menu {

    public FileMenu() {
        super("File");
        final MenuItem open = new MenuItem("Open...");
        open.setOnAction(event -> {
            final Tilemap tilemap = MapManager.get().loadMap();
            if (tilemap != null) {
                Main.root.getCenterView().getEditorViewPane().setTab(tilemap.getId());
            }
        });

        final MenuItem newMap = new MenuItem("New");
        newMap.setOnAction(event -> MapManager.get().createMap());

        final MenuItem save = new MenuItem("Save Current");
        save.setOnAction(event -> {
            final Tab selectedItem = Main.root.getCenterView().getEditorViewPane().getSelectionModel().getSelectedItem();
            if (selectedItem instanceof EditorTab) {
                MapManager.get().saveMap(((EditorTab) selectedItem).getTilemap());
            }
        });

        this.getItems().addAll(
                open,
                newMap,
                new MenuItem("Open Recent"),
                save,
                new MenuItem("Save As..."),
                new MenuItem("Revert")
        );
    }
}
