package com.ktar5.mapeditor.gui.topmenu;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.gui.centerview.editor.tabs.TilemapTab;
import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tilemaps.MapManager;
import com.ktar5.mapeditor.tilemaps.whole.WholeTile;
import com.ktar5.mapeditor.tilemaps.whole.WholeTilemap;
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
            final Tab selectedItem = Main.root.getCenterView().getEditorViewPane().getSelectionModel().getSelectedItem();
            if (selectedItem instanceof TilemapTab) {
                BaseTileset baseTileset = TilesetManager.get().loadTileset();
                BaseTilemap map = MapManager.get().getMap(((TilemapTab) selectedItem).getUuid());
                if(map instanceof WholeTilemap){
                    ((WholeTilemap) map).setTileset(baseTileset);
                    System.out.println("About to draw tilemap");
                    map.draw();
                    System.out.println("Drawing tilemap");
                }
            }
        });


        this.getItems().addAll(
                addTileset
        );
    }


}
