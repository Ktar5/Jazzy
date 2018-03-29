package com.ktar5.mapeditor.gui.centerview.sidebars;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class DetailsSidebar extends Pane {

    public DetailsSidebar() {
        super();
        this.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setMinSize(100, 100);
        this.setMaxWidth(200);
        this.setPrefWidth(200);
        this.getChildren().add(new Pane());
        this.setVisible(true);
    }

}
