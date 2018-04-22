package com.ktar5.jazzy.gui.centerview.tabs;

import com.ktar5.jazzy.coordination.EventCoordinator;
import com.ktar5.jazzy.gui.centerview.EditorPane;
import com.ktar5.jazzy.gui.centerview.sidebars.properties.PropertiesSidebar;
import com.ktar5.jazzy.gui.centerview.sidebars.tileset.TileSelectEvent;
import com.ktar5.jazzy.gui.centerview.sidebars.tileset.TilesetSidebar;
import com.ktar5.jazzy.gui.utils.PixelatedImageView;
import com.ktar5.jazzy.tilemaps.BaseTilemap;
import com.ktar5.jazzy.tilemaps.MapManager;
import com.ktar5.jazzy.tilemaps.sided.SidedTilemap;
import com.ktar5.jazzy.tilemaps.whole.WholeTilemap;
import com.ktar5.jazzy.util.Tabbable;
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
                WholeTilemap tilemap = ((WholeTilemap) getTabbable());
                tilemap.setCurrentData(event.getId(), 0);
            }
        }
    }
    
    @Listener
    public static class SidedTilemapTab extends TilemapTab {
        public SidedTilemapTab(UUID tilemap) {
            super(tilemap);
            EventCoordinator.get().registerListener(this);
        }
        
        @Handler
        public void onSelectTile(TileSelectEvent event) {
            if (event.getTab().equals(this.getTabId())) {
                System.out.println("select");
                Image image = event.getTileset().getTileImages().get(event.getId());
                getTilesetSidebar().getSelectedTileView().setTile(new PixelatedImageView(image));
                SidedTilemap tilemap = ((SidedTilemap) getTabbable());
                System.out.println((event.getId() / 3) + 1);
                tilemap.setCurrentData((event.getId() / 3) + 1);
            }
        }
    }
    
}
