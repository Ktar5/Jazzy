package com.ktar5.mapeditor.util;

import javafx.scene.input.MouseEvent;

public interface Interactable {
    
    /**
     * Called when the mouse is clicked (when the mouse button is released).
     *
     * @param event the MouseEvent that triggered this event.
     */
    public default void onClick(MouseEvent event) {
    
    }
    
    /**
     * Called when the mouse is moved, and the buttons are not pressed.
     *
     * @param event the MouseEvent that triggered this event.
     */
    public default void onMove(MouseEvent event) {
    
    }
    
    /**
     * Called when the mouse is clicked (when the mouse button is released).
     *
     * @param event the MouseEvent that triggered this event.
     */
    public default void onDrag(MouseEvent event) {
    
    }
    
    /**
     * Called on the start of a drag, most implementations simply call
     * "onDrag".
     *
     * @param event the MouseEvent that triggered this event.
     */
    public default void onDragStart(MouseEvent event) {
    
    }
    
    /**
     * Called on the end of a drag, when the mouse button is released.
     * Consider it a way to clean-up anything that happened during a drag.
     *
     * @param event the MouseEvent that triggered this event.
     */
    public default void onDragEnd(MouseEvent event) {
    
    }
    
}
