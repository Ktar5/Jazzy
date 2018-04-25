package com.ktar5.jazzy.editor.gui.centerview.tabs;

import com.ktar5.jazzy.editor.coordination.EventCoordinator;
import com.ktar5.jazzy.editor.gui.centerview.EditorPane;
import com.ktar5.jazzy.editor.gui.centerview.sidebars.properties.PropertiesSidebar;
import com.ktar5.jazzy.editor.gui.centerview.sidebars.tileset.TileSelectEvent;
import com.ktar5.jazzy.editor.gui.centerview.sidebars.tileset.TilesetSidebar;
import com.ktar5.jazzy.editor.gui.utils.PixelatedImageView;
import com.ktar5.jazzy.editor.tilemap.BaseTilemap;
import com.ktar5.jazzy.editor.tilemap.MapManager;
import com.ktar5.jazzy.editor.tilemap.whole.WholeTileLayer;
import com.ktar5.jazzy.editor.util.Tabbable;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import lombok.Getter;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;

import java.util.UUID;

@Getter
public abstract class TilemapTab extends AbstractTab {
    private PropertiesSidebar detailsSidebar;
    private TilesetSidebar tilesetSidebar;
    
    public TilemapTab(UUID tilemap) {
        super(tilemap);
        
        SplitPane sp = new SplitPane();
        sp.getItems().addAll(detailsSidebar = new PropertiesSidebar(MapManager.get().getMap(getTabId()).getRootProperty()),
                this.pane, tilesetSidebar = new TilesetSidebar());
        sp.setDividerPositions(0.2, .75);
        
        
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
        return MapManager.get().getMap(getTabId());
    }
    
    @Override
    public void onSelect() {
        this.getTilesetSidebar().getTilesetView().setTileset(((BaseTilemap) getTabbable()).getTileset());
    }
    
    @Listener
    public static class WholeTilemapTab extends TilemapTab {
        public WholeTilemapTab(UUID tilemap) {
            super(tilemap);
            EventCoordinator.get().registerListener(this);
        }
        
        @Handler
        public void onSelectTile(TileSelectEvent event) {
            if (event.getTab().equals(this.getTabId())) {
                Image image = event.getTileset().getTileImages().get(event.getId());
                getTilesetSidebar().getSelectedTileView().setTile(new PixelatedImageView(image));
                WholeTileLayer tilemap = ((WholeTileLayer) getTabbable());
                tilemap.setCurrentData(event.getId(), 0);
            }
        }
    }
    
}
