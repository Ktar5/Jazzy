package com.ktar5.jazzy.editor.gui.centerview.sidebars.properties;

import com.ktar5.jazzy.editor.properties.ParentProperty;
import com.ktar5.jazzy.editor.properties.Property;
import com.ktar5.jazzy.editor.properties.StringProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.util.Pair;

import java.util.Optional;

public class PropertiesRClickMenu extends ContextMenu {

    public PropertiesRClickMenu(Property property) {
        super();

        MenuItem editMenuItem = new MenuItem();
        if (property instanceof ParentProperty) {
            editMenuItem.setText("Edit Name");
        } else {
            editMenuItem.setText("Edit..");
        }
        editMenuItem.setOnAction(event -> {
            if (property instanceof ParentProperty) {
                Optional<String> name = EditParentDialog.create(((ParentProperty) property));
                name.ifPresent(property::changeName);
            } else {
                Optional<Pair<String, String>> nameAndValueOptional = EditValueDialog.create(((StringProperty) property));
                nameAndValueOptional.ifPresent(nameAndValue -> {
                    ((StringProperty) property).setNameAndValue(nameAndValue.getKey(), nameAndValue.getValue());
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
            if (!(property instanceof ParentProperty)) return;

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Create New Property");
            dialog.setContentText("Please enter the name for the property you want to create:");
            Optional<String> result = dialog.showAndWait();
            if (!result.isPresent()) {
                return;
            }
            ((ParentProperty) property).createProperty(result.get());
        });
        getItems().add(createMenuItem);

        //Delete menu
        MenuItem removeMenuItem = new MenuItem("Delete");
        removeMenuItem.setOnAction(event -> property.getParent().getChildren().remove(property.getName()));
        getItems().add(removeMenuItem);
    }

}
