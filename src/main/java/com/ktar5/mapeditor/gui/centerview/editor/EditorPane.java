package com.ktar5.mapeditor.gui.centerview.editor;

import com.ktar5.mapeditor.tilemaps.whole.WholeTileset;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.Getter;

@Getter
public class EditorPane extends ScrollPane {
    private Group viewport = new Group();
    private double pressedX, pressedY,
            origX, origY;
    private boolean isDragging;

    public EditorPane() {
        super();

        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.prefHeight(-1);
        this.prefWidth(-1);

        viewport.setVisible(true);

        viewport.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            System.out.println("eX: " + e.getX());
        });

        viewport.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }
            Node intersectedNode = event.getPickResult().getIntersectedNode();
            WholeTileset.ImageTestView view = (WholeTileset.ImageTestView) intersectedNode;
            view.incrementImage();
        });

        this.setContent(viewport);

        this.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            //Logger.debug("End dragging");
            if (e.getButton() != MouseButton.MIDDLE) {
                return;
            }
            isDragging = false;
        });

        addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.getButton() != MouseButton.MIDDLE) {
                return;
            }
            if (event.getX() < 0 || event.getY() < 0 || event.getX() > this.getWidth() || event.getY() > this.getHeight()) {
                return;
            }
            if (!isDragging) {
                //Logger.debug("Set canvas initial drag values");
                pressedX = event.getX();
                pressedY = event.getY();
                origX = viewport.getTranslateX();
                origY = viewport.getTranslateY();
                isDragging = true;
            }
            //Logger.debug("Perform drag on canvas");
            viewport.setTranslateX(origX + (event.getX() - pressedX));
            viewport.setTranslateY(origY + (event.getY() - pressedY));
            //System.out.println("X: " + internalPane.getTranslateX() + " Y: " + internalPane.getTranslateY());
            event.consume();
        });

        setOnScroll(event -> {
            //Logger.debug("Scroll");
            double delta = 1.2;

            double scale = viewport.getScaleX(); // currently we only use Y, same value is used for X
            double oldScale = scale;

            if (event.getDeltaY() < 0)
                scale /= delta;
            else
                scale *= delta;

            scale = clamp(scale, .1, 10);

            double f = (scale / oldScale) - 1;

            double dx = (event.getSceneX() - (viewport.getBoundsInParent().getWidth() / 2 + viewport.getBoundsInParent().getMinX()));
            double dy = (event.getSceneY() - (viewport.getBoundsInParent().getHeight() / 2 + viewport.getBoundsInParent().getMinY()));

            viewport.setScaleX(scale);
            viewport.setScaleY(scale);

            // note: pivot value must be untransformed, i. e. without scaling
            viewport.setTranslateX(viewport.getTranslateX() - (f * dx));
            viewport.setTranslateY(viewport.getTranslateY() - (f * dy));

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
