package com.ktar5.mapeditor.gui.topmenu;

import javafx.geometry.Insets;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class TopMenu extends MenuBar {
    private FileMenu fileMenu;
    private EditMenu editMenu;
    
    
    public TopMenu() {
        super();
        this.getMenus().addAll(fileMenu = new FileMenu(), editMenu = new EditMenu(), new MapMenu());
        this.setPadding(new Insets(0, 0, 15, 0));
        VBox.setVgrow(this, Priority.NEVER);
    }
}
