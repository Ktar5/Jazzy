package com.ktar5.mapeditor.gui.centerview.editor;

import javafx.geometry.Insets;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import lombok.Getter;
import org.pmw.tinylog.Logger;

@Getter
public class EditorPane extends Pane {
    private Pane viewport;
    private double pressedX, pressedY,
            origX, origY;
    private boolean isDragging;

    public EditorPane(int x, int y) {
        super();

        viewport = new Pane();
        viewport.setMaxSize(x, y);
        viewport.setPrefSize(x, y);

        //this.setFitToHeight(true);
        //this.setFitToWidth(true);
        this.prefHeight(-1);
        this.prefWidth(-1);

        viewport.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        viewport.setVisible(true);

        this.getChildren().add(viewport);
        //this.setContent(viewport);

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

        this.setOnScroll(event -> {
            Logger.debug("Scroll");
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

    public EditorPane(Pair<Integer, Integer> dimensions) {
        this(dimensions.getKey(), dimensions.getValue());
    }

    public static double clamp(double value, double min, double max) {

        if (Double.compare(value, min) < 0)
            return min;

        if (Double.compare(value, max) > 0)
            return max;

        return value;
    }
}
