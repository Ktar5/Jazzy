package com.ktar5.jazzy.gui.centerview;

import com.ktar5.jazzy.gui.centerview.tabs.AbstractTab;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.UUID;

public class TabHoldingPane extends TabPane {

    public TabHoldingPane() {
        super();
        this.setMaxHeight(Double.MAX_VALUE);
        this.setMaxWidth(Double.MAX_VALUE);

        this.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);

        VBox.setVgrow(this, Priority.ALWAYS);

        this.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
                    if (newTab == null) {
                        return;
                    }
                    if (newTab instanceof AbstractTab) {
                        ((AbstractTab) newTab).onSelect();
                    }
                }
        );
    }

    public void addTab(AbstractTab tab) {
        this.getTabs().add(tab);
    }

    public void setSelectedTab(UUID uuid) {
        for (Tab tab : this.getTabs()) {
            if (tab instanceof AbstractTab && ((AbstractTab) tab).getTabId().equals(uuid)) {
                this.getSelectionModel().select(tab);
                return;
            }
        }
    }

    public void setChanges(UUID uuid, boolean value) {
        getTab(uuid).setEdit(value);
    }

    public AbstractTab getCurrentTab() {
        return ((AbstractTab) this.getSelectionModel().getSelectedItem());
    }

    public Pane getTabDrawingPane(UUID uuid) {
        return getTab(uuid).getViewport();
    }

    public AbstractTab getTab(UUID uuid) {
        AbstractTab tab = ((AbstractTab) this.getSelectionModel().getSelectedItem());
        if (tab != null && tab.getTabId().equals(uuid)) {
            return tab;
        }
        for (Tab forTab : this.getTabs()) {
            if (forTab instanceof AbstractTab && ((AbstractTab) forTab).getTabId().equals(uuid)) {
                return ((AbstractTab) forTab);
            }
        }
        return null;
    }


}
