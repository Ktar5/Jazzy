package com.ktar5.jazzy.gui.topmenu;

import com.ktar5.jazzy.coordination.EditorCoordinator;
import com.ktar5.jazzy.gui.centerview.tabs.TilemapTab;
import com.ktar5.jazzy.gui.dialogs.SelectType;
import com.ktar5.jazzy.tilemaps.BaseTilemap;
import com.ktar5.jazzy.tilemaps.MapManager;
import com.ktar5.jazzy.tilemaps.sided.SidedTileset;
import com.ktar5.jazzy.tilemaps.whole.WholeTileset;
import com.ktar5.jazzy.tileset.BaseTileset;
import com.ktar5.jazzy.tileset.TilesetManager;
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
                        SelectType.getType(WholeTileset.class, SidedTileset.class)
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
