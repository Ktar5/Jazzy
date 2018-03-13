package com.ktar5.mapeditor.javafx.centerview;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class CenterView extends HBox {
    private DetailsPane detailsPane;
    private EditorViewPane editorViewPane;
    private TilePane tilePane;

    public CenterView() {
        super();
        VBox.setVgrow(this, Priority.ALWAYS);
        this.getChildren().addAll(
                detailsPane = new DetailsPane(),
                editorViewPane = new EditorViewPane(),
                tilePane = new TilePane());
    }
}
