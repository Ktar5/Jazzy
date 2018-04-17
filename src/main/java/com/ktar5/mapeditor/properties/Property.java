package com.ktar5.mapeditor.properties;

import javafx.beans.property.SimpleStringProperty;
import org.json.JSONObject;

public abstract class Property {
    public final SimpleStringProperty nameProperty;
    
    public Property(String name) {
        this.nameProperty = new SimpleStringProperty(name);
    }
    
    public String getName() {
        return nameProperty.getValue();
    }
    
    public final void changeName(String newName) {
        this.nameProperty.set(newName);
    }
    
    public abstract void serialize(JSONObject parent);
    
}
