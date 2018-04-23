package com.ktar5.jazzy.plugins.sidedtilemap;

import javafx.scene.image.Image;

import java.util.UUID;

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