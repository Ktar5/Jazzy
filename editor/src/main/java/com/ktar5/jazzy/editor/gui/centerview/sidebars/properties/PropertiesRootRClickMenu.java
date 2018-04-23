package com.ktar5.jazzy.editor.gui.centerview.sidebars.properties;

import com.ktar5.jazzy.editor.properties.RootProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class PropertiesRootRClickMenu extends ContextMenu {

    public PropertiesRootRClickMenu(RootProperty property) {
        MenuItem createMenuItem = new MenuItem("Create");
        createMenuItem.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Create New Property");
            dialog.setContentText("Please enter the name for the property you want to create:");
            Optional<String> result = dialog.showAndWait();
            if (!result.isPresent()) {
                return;
            }
            property.createProperty(result.get());
        });
        getItems().add(createMenuItem);
    }

}
