package com.ktar5.mapeditor.javafx.topmenu;

import javafx.geometry.Insets;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TopMenu extends MenuBar {

    public TopMenu() {
        super(new FileMenu(), new EditMenu());
        this.setPadding(new Insets(0, 0, 15, 0));
        VBox.setVgrow(this, Priority.NEVER);
    }
}
