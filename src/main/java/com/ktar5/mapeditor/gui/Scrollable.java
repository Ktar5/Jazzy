package com.ktar5.mapeditor.gui;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.pmw.tinylog.Logger;

public class Scrollable {
    private double pressedX, pressedY,
            origX, origY;
    private boolean isDragging;


    public void setAll(Node container, Node moving) {
        container.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            //Logger.debug("End dragging");
            if (e.getButton() != MouseButton.MIDDLE) {
                return;
            }
            isDragging = false;
        });

        container.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.getButton() != MouseButton.MIDDLE) {
                return;
            }
            if (event.getX() < 0 || event.getY() < 0) {
                return;
            }
            if (!isDragging) {
                //Logger.debug("Set canvas initial drag values");
                pressedX = event.getX();
                pressedY = event.getY();
                origX = moving.getTranslateX();
                origY = moving.getTranslateY();
                isDragging = true;
            }

            double x = origX + (event.getX() - pressedX);
            double y = origY + (event.getY() - pressedY);
            //Logger.debug("Perform drag on canvas");
            //some condition: moving.getTranslateX() < moving.getBoundsInLocal().getMaxX()
            if (true) {
                moving.setTranslateX(x);
                moving.setTranslateY(y);
            }
            //System.out.println("X: " + internalPane.getTranslateX() + " Y: " + internalPane.getTranslateY());
            event.consume();
        });

        container.setOnScroll(event -> {
            Logger.debug("Scroll");
            double delta = 1.2;

            double scale = moving.getScaleX(); // currently we only use Y, same value is used for X
            double oldScale = scale;

            if (event.getDeltaY() < 0)
                scale /= delta;
            else
                scale *= delta;

            scale = clamp(scale, .1, 10);

            double f = (scale / oldScale) - 1;

            double dx = (event.getSceneX() - (moving.getBoundsInParent().getWidth() / 2 + moving.getBoundsInParent().getMinX()));
            double dy = (event.getSceneY() - (moving.getBoundsInParent().getHeight() / 2 + moving.getBoundsInParent().getMinY()));

            moving.setScaleX(scale);
            moving.setScaleY(scale);

            // note: pivot value must be untransformed, i. e. without scaling
            moving.setTranslateX(moving.getTranslateX() - (f * dx));
            moving.setTranslateY(moving.getTranslateY() - (f * dy));

            event.consume();
        });
    }

    public double clamp(double value, double min, double max) {

        if (Double.compare(value, min) < 0)
            return min;

        if (Double.compare(value, max) > 0)
            return max;

        return value;
    }

}
