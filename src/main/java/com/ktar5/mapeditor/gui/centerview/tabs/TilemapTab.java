package com.ktar5.mapeditor.gui.centerview.tabs;

import com.ktar5.mapeditor.coordination.EventCoordinator;
import com.ktar5.mapeditor.gui.PixelatedImageView;
import com.ktar5.mapeditor.gui.centerview.sidebars.DetailsSidebar;
import com.ktar5.mapeditor.gui.centerview.sidebars.tileset.TileSelectEvent;
import com.ktar5.mapeditor.gui.centerview.sidebars.tileset.TilesetSidebar;
import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tilemaps.MapManager;
import com.ktar5.mapeditor.util.Tabbable;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.Getter;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import org.controlsfx.control.HiddenSidesPane;

import java.util.UUID;

@Getter
@Listener
public class TilemapTab extends AbstractTab {
    private HBox hBox;

    private DetailsSidebar detailsSidebar;
    private TilesetSidebar tilesetSidebar;

    public TilemapTab(UUID tilemap) {
        super(tilemap);
        EventCoordinator.get().registerListener(this);

        this.hBox = new HBox();
        hBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        hBox.getChildren().addAll(
                detailsSidebar = new DetailsSidebar(),
                this.pane,
                tilesetSidebar = new TilesetSidebar());

        this.setContent(hBox);
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
        }
    }

}
