package com.ktar5.mapeditor.properties;

import org.json.JSONObject;

public class RootProperty extends ParentProperty {
    public RootProperty(JSONObject jsonObject) {
        super("properties", jsonObject);
    }
    
    public RootProperty() {
        super("properties");
    }
    
}
