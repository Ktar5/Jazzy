package com.ktar5.mapeditor.properties;

import javafx.beans.property.SimpleStringProperty;
import org.json.JSONObject;

public class StringProperty extends Property {
    public final SimpleStringProperty valueProperty;

    public StringProperty(String name, String value, ParentProperty parent) {
        super(name, parent);
        this.valueProperty = new SimpleStringProperty(value);
    }

    public StringProperty(String name, ParentProperty parent) {
        this(name, "", parent);
    }

    public String getValue() {
        return valueProperty.getValue();
    }

    public void setValue(String value) {
        this.valueProperty.set(value);
    }

    public void setNameAndValue(String name, String value) {
        this.changeName(name);
        this.setValue(value);
    }

    @Override
    public void serialize(JSONObject parent) {
        parent.put(getName(), getValue());
    }

}
