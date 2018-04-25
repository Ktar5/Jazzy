package com.ktar5.jazzy.editor.util;

import com.ktar5.jazzy.editor.coordination.EditorCoordinator;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import com.ktar5.utilities.annotation.dontoverride.DontOverride;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.json.JSONObject;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.util.UUID;

public interface Tabbable extends Interactable, Drawable {
    
    /**
     * Saves the tabbable to a file.
     */
    public void save();
    
    @DontOverride
    /**
     * Saves the tabbable to a chosen file.
     */
    public default void saveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Tileset As..");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json File", "*.json"));
        File saveFile = fileChooser.showSaveDialog(null);
        if (saveFile == null) {
            Logger.info("Tried to save tileset, cancelled or failed");
            return;
        }
        updateSaveFile(saveFile);
        save();
    }
    
    @DontOverride
    /**
     * Sets the tabbable to be marked as having an edit/change.
     */
    public default void setChanged(boolean value) {
        EditorCoordinator.get().getEditor().getTab(getId()).setEdit(value);
    }
    
    /**
     * Should return the boolean set by "setDragging"
     */
    public boolean isDragging();
    
    /**
     * Sets a boolean to the value of "dragging" so that subclasses know when they are
     * being dragged or simply clicked.
     */
    public void setDragging(boolean dragging);
    
    /**
     * Returns a UUID that should be randomly generated in the constructor of any subclass
     */
    public UUID getId();
    
    /**
     * Gets the name of the tabbable.
     */
    public String getName();
    
    /**
     * Returns the save file
     */
    public File getSaveFile();
    
    /**
     * Removes the map from the application.
     */
    public void remove();
    
    /**
     * Change the save file to a different file.
     */
    public void updateSaveFile(File file);
    
    @CallSuper
    /**
     * Turn a Tabbable into a JSONObject manually to assure the
     * proper serialization of the data into json.
     *
     * Note: all implementations must call super
     */
    public JSONObject serialize();
    
}
