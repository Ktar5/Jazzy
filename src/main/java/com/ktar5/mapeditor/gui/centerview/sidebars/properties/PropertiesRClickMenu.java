package com.ktar5.mapeditor.gui.centerview.sidebars.properties;

import com.ktar5.mapeditor.properties.ParentProperty;
import com.ktar5.mapeditor.properties.Property;
import com.ktar5.mapeditor.properties.StringProperty;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.util.Optional;

public class PropertiesRClickMenu extends ContextMenu {
    private final EditingCell cell;

    public PropertiesRClickMenu(EditingCell cell) {
        super();
        this.cell = cell;

        //Edit menu
        MenuItem editMenuItem = new MenuItem("Edit..");
        editMenuItem.setOnAction(event -> {
            if (getProperty() instanceof ParentProperty) {
                Optional<String> name = EditParentDialog.create(((ParentProperty) getProperty()));
                name.ifPresent(string -> getProperty().changeName(string));
            } else {
                Optional<Pair<String, String>> nameAndValueOptional = EditValueDialog.create(((StringProperty) getProperty()));
                nameAndValueOptional.ifPresent(nameAndValue -> {
                    ((StringProperty) getProperty()).setNameAndValue(nameAndValue.getKey(), nameAndValue.getValue());
                });
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
            ((ParentProperty) getProperty()).createProperty(result.get());
        });
        getItems().add(createMenuItem);

        //Delete menu
        MenuItem removeMenuItem = new MenuItem("Delete");
        removeMenuItem.setOnAction(event -> {
            Property parent = getParentProperty();
            if (!(parent instanceof ParentProperty)) return;
            ((ParentProperty) parent).getChildren().remove(getProperty().getName());
        });
        getItems().add(removeMenuItem);
    }

    public Property getParentProperty() {
        return getParentTreeItem().getValue();
    }

    public TreeItem<Property> getParentTreeItem() {
        return getTreeItem().getParent();
    }

    public Property getProperty() {
        return getTreeItem().getValue();
    }

    public TreeItem<Property> getTreeItem() {
        return cell.getTreeTableRow().getTreeItem();
    }

}
