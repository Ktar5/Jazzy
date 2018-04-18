package com.ktar5.mapeditor.gui.centerview.sidebars.properties;

import com.ktar5.mapeditor.properties.ParentProperty;
import com.ktar5.mapeditor.properties.Property;
import javafx.scene.control.*;

import java.util.Optional;

public class PropertiesRClickMenu extends ContextMenu {
    private final EditingCell cell;

    public PropertiesRClickMenu(EditingCell cell) {
        super();
        this.cell = cell;

        //Edit menu
        MenuItem editMenuItem = new MenuItem("Edit..");
        editMenuItem.setOnAction(event -> {
            Property clicked = cell.getTreeTableRow().getTreeItem().getValue();
            if (clicked instanceof ParentProperty) {

            } else {

            }
            //TODO make this


            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Edit Property");
            dialog.setContentText("Please enter the name for the property you want to create:");

            Optional<String> result = dialog.showAndWait();
            if (!result.isPresent()) {
                return;
            }
        });
        getItems().add(editMenuItem);

        //Separate create/delete from edit
        getItems().add(new SeparatorMenuItem());

        //TODO parent property creation
        //Create menu
        MenuItem createMenuItem = new MenuItem("Create");
        createMenuItem.setOnAction(event -> {
            if (!(getProperty() instanceof ParentProperty)) return;

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Create New Property");
            dialog.setContentText("Please enter the name for the property you want to create:");
            Optional<String> result = dialog.showAndWait();
            if (!result.isPresent()) {
                return;
            }
            Property testing = ((ParentProperty) getProperty()).createProperty(result.get());
            TreeItem<Property> newTreeItem = new TreeItem<>(testing);
            getTreeItem().getChildren().add(newTreeItem);
        });
        getItems().add(createMenuItem);

        //Delete menu
        MenuItem removeMenuItem = new MenuItem("Delete");
        removeMenuItem.setOnAction(event -> {
            Property parent = getParentProperty();
            if (!(parent instanceof ParentProperty)) return;
            ((ParentProperty) parent).getChildren().remove(getProperty().getName());
            getTreeItem().getParent().getChildren().remove(getTreeItem());
        });
        getItems().add(removeMenuItem);
    }

    public Property getParentProperty(){
        return getParentTreeItem().getValue();
    }

    public TreeItem<Property> getParentTreeItem(){
        return getTreeItem().getParent();
    }

    public Property getProperty(){
        return getTreeItem().getValue();
    }

    public TreeItem<Property> getTreeItem(){
        return cell.getTreeTableRow().getTreeItem();
    }

}
