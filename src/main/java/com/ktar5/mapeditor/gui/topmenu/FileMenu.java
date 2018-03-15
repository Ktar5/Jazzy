package com.ktar5.mapeditor.gui.topmenu;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.gui.centerview.editor.tabs.TilemapTab;
import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tilemaps.MapManager;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.stage.FileChooser;
import org.pmw.tinylog.Logger;

import java.io.File;

public class FileMenu extends Menu {

    public FileMenu() {
        super("File");
        final MenuItem open = new MenuItem("Open...");
        open.setOnAction(event -> {
            final BaseTilemap baseTilemap = MapManager.get().loadMap();
            if (baseTilemap != null) {
                Main.root.getCenterView().getEditorViewPane().setSelectedTab(baseTilemap.getId());
            }
        });

        final MenuItem newMap = new MenuItem("New");
        newMap.setOnAction(event -> MapManager.get().createMap());

        final MenuItem save = new MenuItem("Save Current");
        save.setOnAction(event -> {
            final Tab selectedItem = Main.root.getCenterView().getEditorViewPane().getSelectionModel().getSelectedItem();
            if (selectedItem instanceof TilemapTab) {
                MapManager.get().saveMap(((TilemapTab) selectedItem).getUuid());
            }
        });

        final MenuItem saveAs = new MenuItem("Save As..");
        saveAs.setOnAction(event -> saveAsDialog());

        this.getItems().addAll(
                open,
                newMap,
                new MenuItem("Open Recent"),
                save,
                saveAs,
                new MenuItem("Revert")
        );
    }

    public void saveAsDialog() {
        final Tab selectedItem = Main.root.getCenterView().getEditorViewPane().getSelectionModel().getSelectedItem();
        if (selectedItem instanceof TilemapTab) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Map As..");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json File", "*.json"));
            final File file = fileChooser.showSaveDialog(null);
            if (file == null) {
                Logger.debug("Something happened here with save as dialog yo");
                return;
            }
            final BaseTilemap map = MapManager.get().getMap(((TilemapTab) selectedItem).getUuid());
            map.updateNameAndFile(file);
            map.save();
            selectedItem.setText(map.getMapName());
        }
    }

}
