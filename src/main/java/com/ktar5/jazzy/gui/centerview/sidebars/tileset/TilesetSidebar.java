package com.ktar5.jazzy.gui.centerview.sidebars.tileset;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class TilesetSidebar extends VBox {
    private TilesetSidebarViewPane tilesetView;
    private SelectedTileView selectedTileView;
    
    public TilesetSidebar() {
        super();
        
        this.setMaxWidth(500);
        this.setPrefWidth(250);
        
        tilesetView = new TilesetSidebarViewPane();
        
        tilesetView.prefHeightProperty().bind(this.widthProperty());
        tilesetView.prefWidthProperty().bind(this.widthProperty());
        
        
        selectedTileView = new SelectedTileView();
        
        selectedTileView.prefHeightProperty().bind(this.widthProperty());
        selectedTileView.maxHeightProperty().bind(this.widthProperty());
        selectedTileView.prefWidthProperty().bind(this.widthProperty());
        
        SplitPane sp = new SplitPane();
        sp.setOrientation(Orientation.VERTICAL);
        sp.getItems().addAll(tilesetView, selectedTileView);
        sp.setDividerPositions(0.6f);
        
        
        this.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        this.getChildren().addAll(sp);
    }
    
}
