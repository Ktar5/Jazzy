package com.ktar5.jazzy.editor.properties;

import org.json.JSONObject;

public class RootProperty extends ParentProperty {
    public RootProperty(JSONObject jsonObject) {
        super("properties", jsonObject, null);
    }
    
    public RootProperty() {
        super("properties", null);
    }
    
}
