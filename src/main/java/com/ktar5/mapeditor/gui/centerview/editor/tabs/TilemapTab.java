package com.ktar5.mapeditor.gui.centerview.editor.tabs;

import com.ktar5.mapeditor.tilemaps.MapManager;
import com.ktar5.mapeditor.util.Tabbable;

import java.util.UUID;

public class TilemapTab extends EditorTab {

    public TilemapTab(UUID uuid) {
        super(uuid);
    }

    @Override
    public void draw() {
        getTabbable().draw();
    }

    @Override
    public Tabbable getTabbable() {
        return MapManager.get().getMap(getUuid());
    }
}
