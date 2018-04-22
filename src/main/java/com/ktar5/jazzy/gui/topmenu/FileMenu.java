package com.ktar5.jazzy.gui.topmenu;

import com.ktar5.jazzy.coordination.EditorCoordinator;
import com.ktar5.jazzy.tilemaps.BaseTilemap;
import com.ktar5.jazzy.tilemaps.MapManager;
import com.ktar5.jazzy.tileset.BaseTileset;
import com.ktar5.jazzy.tileset.TilesetManager;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

public class FileMenu extends Menu {
    
    public FileMenu() {
        super("File");
        final Menu openMenu = new Menu("Open..");
        
        final MenuItem openTilemap = new MenuItem("Open Tilemap");
        openTilemap.setOnAction(event -> {
            final BaseTilemap baseTilemap = MapManager.get().loadMap();
            if (baseTilemap != null) {
                EditorCoordinator.get().getEditor().setSelectedTab(baseTilemap.getId());
            }
        });
        openTilemap.setAccelerator(KeyCombination.keyCombination("SHORTCUT+O"));
        
        final MenuItem openTileset = new MenuItem("Open Tileset");
        openTileset.setOnAction(event -> {
            final BaseTileset baseTileset = TilesetManager.get().loadTileset();
            if (baseTileset != null) {
                EditorCoordinator.get().getEditor().setSelectedTab(baseTileset.getId());
            }
        });
        openTileset.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+O"));
        
        openMenu.getItems().addAll(openTilemap, openTileset);
        
        
        final MenuItem newMap = new MenuItem("New Tilemap");
        newMap.setOnAction(event -> MapManager.get().createMap());
        newMap.setAccelerator(KeyCombination.keyCombination("SHORTCUT+N"));
        
        final MenuItem newTileset = new MenuItem("New Tileset");
        newTileset.setOnAction(event -> TilesetManager.get().createTileset());
        newTileset.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+N"));
        
        final Menu newMenu = new Menu("New...");
        newMenu.getItems().addAll(newMap, newTileset);
        
        
        final MenuItem save = new MenuItem("Save Current");
        save.setOnAction(event -> {
            if (EditorCoordinator.get().getCurrentTab() != null)
                EditorCoordinator.get().getCurrentTab().getTabbable().save();
        });
        save.setAccelerator(KeyCombination.keyCombination("SHORTCUT+S"));
        
        final MenuItem saveAs = new MenuItem("Save As..");
        saveAs.setOnAction(event -> {
            if (EditorCoordinator.get().getCurrentTab() != null)
                EditorCoordinator.get().getCurrentTab().getTabbable().saveAs();
        });
        saveAs.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+S"));
        
        this.getItems().addAll(
                newMenu,
                openMenu,
                save,
                saveAs,
                new MenuItem("Open Recent"),
                new MenuItem("Revert")
        );
    }
    
}
