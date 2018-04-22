package com.ktar5.jazzy.util;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class ClipUtil {
    
    public static void clip(Pane viewport) {
        // create rectangle with sizes of pane,
        // don't need to set x and y explicitly
        // as positions of clip node are relative to parent node
        // (to pane in our case)
        Rectangle clipRect = new Rectangle(viewport.getWidth(), viewport.getHeight());
        
        // bind properties so height and width of rect
        // changes according pane's width and height
        clipRect.heightProperty().bind(viewport.heightProperty());
        clipRect.widthProperty().bind(viewport.widthProperty());
        
        // set rect as clip rect
        viewport.setClip(clipRect);
    }
    
}
