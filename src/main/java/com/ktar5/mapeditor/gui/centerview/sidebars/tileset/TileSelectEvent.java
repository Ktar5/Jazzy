package com.ktar5.mapeditor.gui.centerview.sidebars.tileset;

import com.ktar5.mapeditor.tileset.BaseTileset;
import com.ktar5.mapeditor.util.EditorEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class TileSelectEvent extends EditorEvent {
    private UUID tab;
    
    private int id;
    private BaseTileset tileset;
    
}
