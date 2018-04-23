package com.ktar5.jazzy.libgdx.properties;

import com.sun.javafx.collections.ObservableMapWrapper;
import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

@Getter
public class ParentProperty extends Property {
    private ObservableMapWrapper<String, Property> children;
    
    public ParentProperty(String name, JSONObject parentJson, ParentProperty parentProperty) {
        this(name, parentProperty);
        deserialize(parentJson);
    }
    
    public ParentProperty(String name, ParentProperty parentProperty) {
        super(name, parentProperty);
        children = new ObservableMapWrapper<>(new HashMap<>());
    }
    
    public void deserialize(JSONObject property) {
        for (String key : property.keySet()) {
            try {
                JSONObject jsonObject = property.getJSONObject(key);
                children.put(key, new ParentProperty(key, jsonObject, this));
            } catch (JSONException e) {
                children.put(key, new StringProperty(key, property.getString(key), this));
            }
        }
    }
    
    public final boolean hasChildren() {
        return !getChildren().isEmpty();
    }
    
    public Property createProperty(String key) {
        return createProperty(key, "");
    }
    
    //TODO Possibly setup recursion
    public Property createProperty(String key, String value) throws IllegalArgumentException {
        String[] split = key.split(Pattern.quote("."));
        
        //loop until last node
        ParentProperty parentProperty = this;
        for (int i = 0; i < split.length - 1; i++) {
            if (!parentProperty.getChildren().containsKey(split[i])) {
                parentProperty.getChildren().put(split[i], new ParentProperty(split[i], parentProperty));
                parentProperty = (ParentProperty) parentProperty.getChildren().get(split[i]);
            } else {
                Property property = parentProperty.getChildren().get(split[i]);
                if (!(property instanceof ParentProperty)) {
                    throw new IllegalArgumentException(
                            "The key: '" + key + "' for property '" + getName() + "' references an existing non-parent node." +
                                    "\nFound error at node " + i + ": '" + split[i] + "'.");
                }
                parentProperty = (ParentProperty) property;
            }
        }
        String propName = split[split.length - 1];
        if (parentProperty.getChildren().containsKey(propName)) {
            throw new IllegalArgumentException(
                    "The property: '" + key + "' for property '" + getName() + "' already exists." +
                            "\nFound error at node " + (split.length - 1) + ": '" + propName + "'.");
        } else {
            StringProperty stringProperty = new StringProperty(propName, value, parentProperty);
            parentProperty.getChildren().put(propName, stringProperty);
            return stringProperty;
        }
    }
    
    public String getProperty(String key) throws IllegalArgumentException {
        String[] split = key.split(".");
        
        //loop until last node
        ParentProperty parentProperty = this;
        for (int i = 0; i < split.length - 1; i++) {
            if (!parentProperty.getChildren().containsKey(split[i])) {
                throw new IllegalArgumentException(
                        "The property: '" + key + "' for property '" + getName() + "' does not exist." +
                                "\nFound error at node " + i + ": '" + split[i] + "'.");
            }
            Property property = parentProperty.getChildren().get(split[i]);
            if (!(property instanceof ParentProperty)) {
                throw new IllegalArgumentException(
                        "The property: '" + key + "' for property '" + getName() + "' contains a non-parent node." +
                                "\nFound error at node " + i + ": '" + split[i] + "'.");
            }
            parentProperty = (ParentProperty) property;
        }
        Property property = parentProperty.getChildren().get(split[split.length - 1]);
        if (!(property instanceof StringProperty)) {
            throw new IllegalArgumentException(
                    "The property: '" + key + "' for property '" + getName() + "' contains a non-parent node." +
                            "\nFound error at node " + (split.length - 1) + ": '" + split[split.length - 1] + "'.");
        }
        return ((StringProperty) property).getValue();
    }
    
    @Override
    public void serialize(JSONObject parent) {
        JSONObject jsonObject = new JSONObject();
        for (Property property : getChildren().values()) {
            property.serialize(jsonObject);
        }
        parent.put(getName(), jsonObject);
    }
    
}
