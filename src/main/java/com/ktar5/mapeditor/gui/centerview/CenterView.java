package com.ktar5.mapeditor.gui.centerview;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CenterView extends HBox {

    public CenterView() {
        super();
        VBox.setVgrow(this, Priority.ALWAYS);
        this.getChildren().addAll(new DetailsPane(), new ViewPane());
    }
}
