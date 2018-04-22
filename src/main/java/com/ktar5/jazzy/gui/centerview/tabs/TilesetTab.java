package com.ktar5.jazzy.gui.centerview.tabs;

import com.ktar5.jazzy.gui.centerview.EditorPane;
import com.ktar5.jazzy.gui.centerview.sidebars.properties.PropertiesSidebar;
import com.ktar5.jazzy.tileset.TilesetManager;
import com.ktar5.jazzy.util.Tabbable;
import javafx.scene.control.SplitPane;
import lombok.Getter;

import java.util.UUID;

@Getter
public class TilesetTab extends AbstractTab {
    private PropertiesSidebar propertiesSidebar;
    
    public TilesetTab(UUID tilemap) {
        super(tilemap);
        propertiesSidebar = new PropertiesSidebar(TilesetManager.get().getTileset(getTabId()).getRootProperty());
        
        SplitPane sp = new SplitPane();
        sp.getItems().addAll(propertiesSidebar, this.pane);
        sp.setDividerPositions(.2);
        
        this.setContent(sp);
    }
    
    @Override
    protected EditorPane getEditorPane() {
        return new EditorPane(getTabbable().getDimensions());
    }
    
    @Override
    public void draw() {
        getTabbable().draw(getViewport());
    }
    
    @Override
    public Tabbable getTabbable() {
        return TilesetManager.get().getTileset(getTabId());
    }
    
    @Override
    public void onSelect() {
    
    }
    
    
}
