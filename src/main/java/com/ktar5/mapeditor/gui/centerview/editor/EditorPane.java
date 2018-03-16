package com.ktar5.mapeditor.gui.centerview.editor;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.Getter;
import org.pmw.tinylog.Logger;

@Getter
public class EditorPane extends ScrollPane {
    private Pane internalPane = new Pane();
    private double pressedX, pressedY,
            origX, origY;
    private boolean isDragging;

    public EditorPane() {
        super();

        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.prefHeight(-1);
        this.prefWidth(-1);

        internalPane.setVisible(true);

        this.setContent(internalPane);

        this.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            //Logger.debug("End dragging");
            if(e.getButton() != MouseButton.MIDDLE){
                return;
            }
            isDragging = false;
        });

        addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if(event.getButton() != MouseButton.MIDDLE){
                return;
            }
            if (!isDragging) {
                //Logger.debug("Set canvas initial drag values");
                pressedX = event.getX();
                pressedY = event.getY();
                origX = internalPane.getTranslateX();
                origY = internalPane.getTranslateY();
                isDragging = true;
            }
            //Logger.debug("Perform drag on canvas");
            internalPane.setTranslateX(origX + (event.getX() - pressedX));
            internalPane.setTranslateY(origY + (event.getY() - pressedY));
            System.out.println("X: " + internalPane.getTranslateX() + " Y: " + internalPane.getTranslateY());
            event.consume();
        });

        setOnScroll(event -> {
            //Logger.debug("Scroll");
            double delta = 1.2;

            double scale = internalPane.getScaleX(); // currently we only use Y, same value is used for X
            double oldScale = scale;

            if (event.getDeltaY() < 0)
                scale /= delta;
            else
                scale *= delta;

            scale = clamp(scale, .1, 10);

            double f = (scale / oldScale) - 1;

            double dx = (event.getSceneX() - (internalPane.getBoundsInParent().getWidth() / 2 + internalPane.getBoundsInParent().getMinX()));
            double dy = (event.getSceneY() - (internalPane.getBoundsInParent().getHeight() / 2 + internalPane.getBoundsInParent().getMinY()));

            internalPane.setScaleX(scale);
            internalPane.setScaleY(scale);

            // note: pivot value must be untransformed, i. e. without scaling
            internalPane.setTranslateX(internalPane.getTranslateX() - (f * dx));
            internalPane.setTranslateY(internalPane.getTranslateY() - (f * dy));

            event.consume();
        });


    }

    public static double clamp(double value, double min, double max) {

        if (Double.compare(value, min) < 0)
            return min;

        if (Double.compare(value, max) > 0)
            return max;

        return value;
    }
}
