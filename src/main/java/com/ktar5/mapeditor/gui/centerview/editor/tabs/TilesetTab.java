package com.ktar5.mapeditor.gui.centerview.editor.tabs;

import com.ktar5.mapeditor.gui.centerview.editor.EditorCanvas;
import com.ktar5.mapeditor.tileset.TilesetManager;
import lombok.Getter;

import java.util.UUID;

@Getter
public class TilesetTab extends EditorTab {
    public TilesetTab(UUID tileset) {
        super(tileset);
    }

    @Override
    public void draw() {
        TilesetManager.get().getTileset(getUuid()).draw();
    }

    @Override
    EditorCanvas getCanvas() {
        return TilesetManager.get().getTileset(getUuid()).getCanvas();
    }

    @Override
    public String getName() {
        return TilesetManager.get().getTileset(getUuid()).getTilesetFile().getName();
    }

    @Override
    void saveCurrent() {
        TilesetManager.get().saveTileset(getUuid());
    }

    @Override
    void removeCurrent() {
        TilesetManager.get().remove(getUuid());
    }

}
