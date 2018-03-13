package com.ktar5.mapeditor.javafx.centerview;

import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.UUID;

public class ViewPane extends TabPane {

    public ViewPane() {
        super();

        this.setMaxHeight(Double.MAX_VALUE);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPrefHeight(650);
        this.setPrefWidth(650);
        //TODO:
        this.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);

        HBox.setHgrow(this, Priority.ALWAYS);
        this.getTabs().addAll(new EditorTab(UUID.randomUUID()), new EditorTab(UUID.randomUUID()),
                new EditorTab(UUID.randomUUID()));
    }

    public void createTab() {
        //todo
    }


}
