package com.ktar5.mapeditor.gui.centerview.tabs;

import com.ktar5.mapeditor.coordination.EventCoordinator;
import com.ktar5.mapeditor.gui.centerview.EditorPane;
import com.ktar5.mapeditor.gui.centerview.sidebars.DetailsSidebar;
import com.ktar5.mapeditor.gui.centerview.sidebars.tileset.TileSelectEvent;
import com.ktar5.mapeditor.gui.centerview.sidebars.tileset.TilesetSidebar;
import com.ktar5.mapeditor.gui.utils.PixelatedImageView;
import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tilemaps.MapManager;
import com.ktar5.mapeditor.tilemaps.whole.WholeTilemap;
import com.ktar5.mapeditor.util.Tabbable;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import lombok.Getter;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;

import java.util.UUID;

@Getter
@Listener
public class WholeTilemapTab extends AbstractTab {
    private DetailsSidebar detailsSidebar;
    private TilesetSidebar tilesetSidebar;

    public WholeTilemapTab(UUID tilemap) {
        super(tilemap);
        EventCoordinator.get().registerListener(this);

        SplitPane sp = new SplitPane();
        sp.getItems().addAll(detailsSidebar = new DetailsSidebar(), this.pane, tilesetSidebar = new TilesetSidebar());
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
