package com.ktar5.mapeditor.gui.centerview.sidebars.properties;

import com.ktar5.mapeditor.properties.Property;
import com.ktar5.mapeditor.properties.RootProperty;
import javafx.scene.control.TreeTableCell;

final class PropertyCell extends TreeTableCell<Property, String> {
    private PropertiesRootRClickMenu rootMenu;

    PropertyCell(RootProperty rootProperty) {
        rootMenu = new PropertiesRootRClickMenu(rootProperty);
    }

    @Override
    public final void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null || getTreeTableRow().getTreeItem() == null) {
            setText(null);
            setGraphic(null);
            setContextMenu(rootMenu);
        } else {
            setText(item);
            setContextMenu(new PropertiesRClickMenu(getTreeTableRow().getItem()));
        }
    }

}

