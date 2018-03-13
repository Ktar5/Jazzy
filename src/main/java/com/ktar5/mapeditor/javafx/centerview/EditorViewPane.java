package com.ktar5.mapeditor.javafx.centerview;

import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.UUID;

public class EditorViewPane extends TabPane {

    public EditorViewPane() {
        super();

        this.setMaxHeight(Double.MAX_VALUE);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPrefHeight(650);
        this.setPrefWidth(650);
        //TODO:
        this.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);

        HBox.setHgrow(this, Priority.ALWAYS);
    }

    public void createTab(UUID uuid) {
        this.getTabs().add(new EditorTab(uuid));
    }


}
