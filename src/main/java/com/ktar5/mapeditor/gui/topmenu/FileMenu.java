package com.ktar5.mapeditor.gui.topmenu;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.gui.dialogs.WholeOrComposite;
import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tilemaps.MapManager;
import com.ktar5.mapeditor.tilemaps.composite.CompositeTilemap;
import com.ktar5.mapeditor.tilemaps.composite.CompositeTileset;
import com.ktar5.mapeditor.tilemaps.whole.WholeTile;
import com.ktar5.mapeditor.tilemaps.whole.WholeTilemap;
import com.ktar5.mapeditor.tilemaps.whole.WholeTileset;
import com.ktar5.mapeditor.tileset.BaseTileset;
import com.ktar5.mapeditor.tileset.TilesetManager;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

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
        openTilemap.setAccelerator(KeyCombination.keyCombination("SHORTCUT+O"));

        final MenuItem openTileset = new MenuItem("Open Tileset");
        openTileset.setOnAction(event -> {
            final BaseTileset baseTileset = TilesetManager.get().loadTileset();
            if (baseTileset != null) {
                Main.root.getCenterView().getEditorViewPane().setSelectedTab(baseTileset.getId());
            }
        });
        openTileset.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+O"));

        final MenuItem newMap = new MenuItem("New Tilemap");
        newMap.setOnAction(event -> MapManager.get().createMap());
        newMap.setAccelerator(KeyCombination.keyCombination("SHORTCUT+N"));

        final MenuItem newTileset = new MenuItem("New Tileset");
        newTileset.setOnAction(event -> TilesetManager.get().createTileset());
        newTileset.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+N"));

        final MenuItem save = new MenuItem("Save Current");
        save.setOnAction(event -> {
            if (Main.root.getCurrentTab() != null)
                Main.root.getCurrentTab().getTabbable().save();
        });
        save.setAccelerator(KeyCombination.keyCombination("SHORTCUT+S"));

        final MenuItem saveAs = new MenuItem("Save As..");
        saveAs.setOnAction(event -> {
            if (Main.root.getCurrentTab() != null)
                Main.root.getCurrentTab().getTabbable().saveAs();
        });
        saveAs.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+S"));

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
