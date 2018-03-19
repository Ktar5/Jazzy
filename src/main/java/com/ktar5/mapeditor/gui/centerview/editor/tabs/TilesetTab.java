package com.ktar5.mapeditor.gui.centerview.editor.tabs;

import com.ktar5.mapeditor.tileset.TilesetManager;
import com.ktar5.mapeditor.util.Tabbable;
import lombok.Getter;

import java.util.UUID;

@Getter
public class TilesetTab extends EditorTab {
    public TilesetTab(UUID tileset) {
        super(tileset);
    }

    @Override
    public void draw() {
        getTabbable().draw();
    }

    @Override
    public Tabbable getTabbable() {
        return TilesetManager.get().getTileset(getUuid());
    }

}
