package com.ktar5.mapeditor.properties;

import javafx.beans.property.SimpleStringProperty;
import org.json.JSONObject;

public abstract class Property {
    public final ParentProperty parent;
    public final SimpleStringProperty nameProperty;

    public Property(String name, ParentProperty parent) {
        this.parent = parent;
        this.nameProperty = new SimpleStringProperty(name);
    }

    public String getPath() {
        StringBuilder path = new StringBuilder();
        Property node = this;
        while (node != null) {
            path.insert(0, ".");
            path.insert(0, node.getName());
            node = node.getParent();
        }
        path.deleteCharAt(path.length() -1);
        return path.toString();
    }

    public ParentProperty getParent() {
        return this.parent;
    }

    public String getName() {
        return nameProperty.getValue();
    }

    public final void changeName(String newName) {
        this.nameProperty.set(newName);
    }

    public abstract void serialize(JSONObject parent);

}
