package com.ktar5.jazzy.libgdx.map;

import com.ktar5.jazzy.libgdx.properties.RootProperty;
import org.json.JSONObject;

public abstract class JazzyAbstractLayer {
    private String name = "";
    private float opacity = 1.0f;
    private boolean visible = true;
    private float offsetX;
    private float offsetY;
    private float renderOffsetX;
    private float renderOffsetY;
    private boolean renderOffsetDirty = true;
    private RootProperty properties;
    
    /**
     * @return map's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name new name for the map
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return map's opacity
     */
    public float getOpacity() {
        return opacity;
    }
    
    /**
     * @param opacity new opacity for the map
     */
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
    
    /**
     * @return map's x offset
     */
    public float getOffsetX() {
        return offsetX;
    }
    
    /**
     * @param offsetX new x offset for the map
     */
    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
        invalidateRenderOffset();
    }
    
    /**
     * @return map's y offset
     */
    public float getOffsetY() {
        return offsetY;
    }
    
    /**
     * @param offsetY new y offset for the map
     */
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        invalidateRenderOffset();
    }
    
    /**
     * @return the map's x render offset, this takes into consideration all parent layers' offsets
     **/
    public float getRenderOffsetX() {
        if (renderOffsetDirty) calculateRenderOffsets();
        return renderOffsetX;
    }
    
    /**
     * @return the map's y render offset, this takes into consideration all parent layers' offsets
     **/
    public float getRenderOffsetY() {
        if (renderOffsetDirty) calculateRenderOffsets();
        return renderOffsetY;
    }
    
    /**
     * set the renderOffsetDirty state to true, when this map or any parents' offset has changed
     **/
    public void invalidateRenderOffset() {
        renderOffsetDirty = true;
    }
    
    /**
     * @return whether the map is visible or not
     */
    public boolean isVisible() {
        return visible;
    }
    
    /**
     * @param visible toggles map's visibility
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    /**
     * @return map's set of properties
     */
    public RootProperty getProperties() {
        return properties;
    }
    
    protected void loadProperties(JSONObject json) {
        this.properties = new RootProperty(json);
    }
    
    protected void calculateRenderOffsets() {
        renderOffsetX = offsetX;
        renderOffsetY = offsetY;
        renderOffsetDirty = false;
    }
    
}
