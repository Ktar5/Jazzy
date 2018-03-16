package com.ktar5.mapeditor.gui.centerview.editor.tabs;

import com.ktar5.mapeditor.gui.centerview.editor.EditorCanvas;
import com.ktar5.mapeditor.tilemaps.MapManager;

import java.util.UUID;

public class TilemapTab extends EditorTab {

    public TilemapTab(UUID uuid) {
        super(uuid);
    }

    @Override
    public void draw() {
        MapManager.get().getMap(getUuid()).draw();
    }

    @Override
    EditorCanvas getCanvas() {
        return MapManager.get().getMap(getUuid()).getCanvas();
    }

    @Override
    public String getName() {
        return MapManager.get().getMap(getUuid()).getMapName();
    }

    @Override
    void saveCurrent() {
        MapManager.get().saveMap(getUuid());
    }

    @Override
    void removeCurrent() {
        MapManager.get().remove(getUuid());
    }
}
