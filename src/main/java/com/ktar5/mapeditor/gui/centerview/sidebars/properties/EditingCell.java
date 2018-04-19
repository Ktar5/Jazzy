package com.ktar5.mapeditor.gui.centerview.sidebars.properties;

import com.ktar5.mapeditor.properties.Property;
import com.ktar5.mapeditor.properties.RootProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;
import javafx.scene.input.KeyCode;

final class EditingCell extends TreeTableCell<Property, String> {
    private PropertiesRClickMenu menu;
    private PropertiesRootRClickMenu rootMenu;
    private TextField textField;

    EditingCell(RootProperty rootProperty) {
        menu = new PropertiesRClickMenu(this);
        rootMenu = new PropertiesRootRClickMenu(rootProperty);
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getItem());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null || getTreeTableRow().getTreeItem() == null) {
            setText(null);
            setGraphic(null);
            setContextMenu(rootMenu);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setContextMenu(menu);
            }
        }
    }

    @Override
    public void commitEdit(String text) {
        super.commitEdit(text);
        setText(textField.getText());
        setGraphic(null);
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                commitEdit(textField.getText());
            }
        });
        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

    }

    private String getString() {
        return getItem() == null ? "" : getItem();
    }
}

