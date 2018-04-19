package com.ktar5.mapeditor.gui.centerview.sidebars.properties;

import com.ktar5.mapeditor.properties.ParentProperty;
import com.ktar5.mapeditor.properties.Property;
import com.ktar5.mapeditor.properties.RootProperty;
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
            Property testing = property.createProperty(result.get());
            //TreeItem<Property> newTreeItem = new TreeItem<>(testing);
            //getTreeItem().getChildren().add(newTreeItem);
        });
        getItems().add(createMenuItem);
    }

}
