package com.ktar5.mapeditor.gui.centerview.editor;

import javafx.scene.canvas.Canvas;

public class EditorCanvas extends Canvas {
    private static final double MAX_SCALE = 10.0d;
    private static final double MIN_SCALE = .1d;

    double pressedX, pressedY;
    double origX, origY;
    boolean isDragging = false;

    public EditorCanvas() {
        throw new RuntimeException("Don't initialize it like this, use EditorCanvas(double, double)");
    }

    public EditorCanvas(double width, double height) {
        super(width, height);


        setOnScroll(event -> {
            double delta = 1.2;

            double scale = this.getScaleX(); // currently we only use Y, same value is used for X
            double oldScale = scale;

            if (event.getDeltaY() < 0)
                scale /= delta;
            else
                scale *= delta;

            scale = clamp(scale, MIN_SCALE, MAX_SCALE);

            double f = (scale / oldScale) - 1;

            double dx = (event.getSceneX() - (this.getBoundsInParent().getWidth() / 2 + this.getBoundsInParent().getMinX()));
            double dy = (event.getSceneY() - (this.getBoundsInParent().getHeight() / 2 + this.getBoundsInParent().getMinY()));

            this.setScaleX(scale);
            this.setScaleY(scale);

            // note: pivot value must be untransformed, i. e. without scaling
            this.setPivot(f * dx, f * dy);

            setInt();

            event.consume();
        });

    }

    public void setInt(){
        this.setTranslateX((int)this.getTranslateX());
        this.setTranslateY((int)this.getTranslateY());
    }

    public void setPivot(double x, double y) {
        setTranslateX(getTranslateX() - x);
        setTranslateY(getTranslateY() - y);
    }

    public static double clamp(double value, double min, double max) {

        if (Double.compare(value, min) < 0)
            return min;

        if (Double.compare(value, max) > 0)
            return max;

        return value;
    }


}
