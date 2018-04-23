package com.ktar5.jazzy.editor.gui.topmenu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class EditMenu extends Menu {
    
    public EditMenu() {
        super("Edit");
        this.getItems().addAll(
                new MenuItem("Undo"),
                new MenuItem("Redo")
        );
    }
    
}
