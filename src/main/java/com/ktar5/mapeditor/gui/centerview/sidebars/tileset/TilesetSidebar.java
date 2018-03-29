package com.ktar5.mapeditor.gui.centerview.sidebars.tileset;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class TilesetSidebar extends VBox {
    private TilesetSidebarTabScrollPane tilesetView;
    private SelectedTileView selectedTileView;

    public TilesetSidebar() {
        super();

        System.out.println("Created");

        this.setMaxWidth(250);
        this.setPrefWidth(250);
        tilesetView = new TilesetSidebarTabScrollPane();
        tilesetView.setVisible(true);

        selectedTileView = new SelectedTileView();
        selectedTileView.setVisible(true);

        this.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        this.getChildren().addAll(tilesetView, selectedTileView);
    }

}
