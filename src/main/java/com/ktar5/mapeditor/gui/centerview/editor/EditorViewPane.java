package com.ktar5.mapeditor.gui.centerview.editor;

import javafx.scene.control.Tab;
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
        this.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);

        HBox.setHgrow(this, Priority.ALWAYS);
    }

    public void createTab(UUID uuid) {
        this.getTabs().add(new EditorTab(uuid));
    }

    public void setTab(UUID uuid) {
        for (Tab tab : this.getTabs()) {
            if (tab instanceof EditorTab && ((EditorTab) tab).getTilemap().equals(uuid)) {
                this.getSelectionModel().select(tab);
                return;
            }
        }
    }

    public void setChanges(UUID uuid, boolean value) {
        EditorTab tab = ((EditorTab) this.getSelectionModel().getSelectedItem());
        if (tab.getTilemap().equals(uuid)) {
            tab.setEdit(value);
            return;
        }
        for (Tab itab : this.getTabs()) {
            if (itab instanceof EditorTab && ((EditorTab) itab).getTilemap().equals(uuid)) {
                ((EditorTab) itab).setEdit(value);
                return;
            }
        }

    }


}
