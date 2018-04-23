package com.ktar5.jazzy.editor.gui.centerview.sidebars.tileset;

import com.ktar5.jazzy.editor.tileset.BaseTileset;
import com.ktar5.jazzy.editor.util.EditorEvent;
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
