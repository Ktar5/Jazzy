package com.ktar5.jazzy.editor.util;

import javafx.scene.layout.Pane;

public interface Drawable {
    /**
     * This method should draw whatever the Tabbable represents to
     * the pane passed as a parameter.
     *
     * @param pane the pane to be drawn to.
     */
    public void draw(Pane pane);
}
