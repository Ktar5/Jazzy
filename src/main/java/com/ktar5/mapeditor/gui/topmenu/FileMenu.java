package com.ktar5.mapeditor.gui.topmenu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class FileMenu extends Menu {

    public FileMenu() {
        super("File");
        this.getItems().addAll(
                new MenuItem("Open..."),
                new MenuItem("New"),
                new MenuItem("Open Recent"),
                new MenuItem("Save"),
                new MenuItem("Save As..."),
                new MenuItem("Revert")
        );
    }
}
