package com.ktar5.jazzy.editor.gui.topmenu;

import com.ktar5.jazzy.editor.coordination.EditorCoordinator;
import com.ktar5.jazzy.editor.gui.centerview.tabs.TilemapTab;
import com.ktar5.jazzy.editor.tilemap.BaseTilemap;
import com.ktar5.jazzy.editor.tilemap.MapManager;
import com.ktar5.jazzy.editor.tileset.BaseTileset;
import com.ktar5.jazzy.editor.tileset.TilesetManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;

public class MapMenu extends Menu {
    
    public MapMenu() {
        super("Map");
        
        final MenuItem addTileset = new MenuItem("Add Tileset");
        addTileset.setOnAction((ActionEvent event) -> {
            final Tab selectedItem = EditorCoordinator.get().getEditor().getSelectionModel().getSelectedItem();
            if (selectedItem instanceof TilemapTab) {
                BaseTileset baseTileset = TilesetManager.get().loadTileset(
                );
                BaseTilemap map = MapManager.get().getMap(((TilemapTab) selectedItem).getTabId());
                map.setTileset(baseTileset);
                System.out.println("About to draw tilemap");
                map.draw(EditorCoordinator.get().getEditor().getTabDrawingPane(map.getId()));
                System.out.println("Drawing tilemap");
            }
        });
        
        
        this.getItems().addAll(
                addTileset
        );
    }
    
    
}
