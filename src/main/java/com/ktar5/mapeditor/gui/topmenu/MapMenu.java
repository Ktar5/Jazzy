package com.ktar5.mapeditor.gui.topmenu;

import com.ktar5.mapeditor.coordination.EditorCoordinator;
import com.ktar5.mapeditor.gui.centerview.tabs.TilemapTab;
import com.ktar5.mapeditor.gui.dialogs.WholeOrComposite;
import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tilemaps.MapManager;
import com.ktar5.mapeditor.tilemaps.sided.SidedTileset;
import com.ktar5.mapeditor.tilemaps.whole.WholeTileset;
import com.ktar5.mapeditor.tileset.BaseTileset;
import com.ktar5.mapeditor.tileset.TilesetManager;
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
                        WholeOrComposite.getType(WholeTileset.class, SidedTileset.class)
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
