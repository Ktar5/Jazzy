package com.ktar5.mapeditor.gui.utils;

import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResizableGrid extends Pane {
    // This is to make the stroke be drawn 'on pixel'.
    private static final double HALF_PIXEL_OFFSET = -0.5;

    private final Canvas canvas = new Canvas();
    private boolean needsLayout;

    private final DoubleProperty zoomProperty = new DoublePropertyBase(1) {
        @Override
        public Object getBean() {
            return ResizableGrid.this;
        }

        @Override
        public String getName() {
            return "zoom";
        }
    };


    private final StyleableObjectProperty<Paint> gridColor = new StyleableObjectProperty<Paint>(
            Color.rgb(222, 248, 255)) {

        @Override
        public CssMetaData<? extends Styleable, Paint> getCssMetaData() {
            return StyleableProperties.GRID_COLOR;
        }

        @Override
        public Object getBean() {
            return ResizableGrid.this;
        }

        @Override
        public String getName() {
            return "gridColor";
        }

        @Override
        protected void invalidated() {
            needsLayout = true;
            requestLayout();
        }
    };

    private final StyleableObjectProperty<Number> gridSpacing = new StyleableObjectProperty<Number>(16) {

        @Override
        public CssMetaData<? extends Styleable, Number> getCssMetaData() {
            return StyleableProperties.GRID_SPACING;
        }

        @Override
        public Object getBean() {
            return ResizableGrid.this;
        }

        @Override
        public String getName() {
            return "gridSpacing";
        }

        @Override
        protected void invalidated() {
            needsLayout = true;
            requestLayout();
        }
    };

    public ResizableGrid(Pane region, DoubleProperty regionZoomProperty) {
        getStyleClass().add("graph-grid");
        getChildren().add(canvas);
        zoomProperty.bind(regionZoomProperty);

        region.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            this.setTranslateX(newValue.getMinX());
            this.setTranslateY(newValue.getMinY());
        });

        regionZoomProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue == null || observable == null) {
                return;
            }
            needsLayout = true;
            layoutChildren();
        });

        this.setPickOnBounds(false);
        this.setMouseTransparent(true);

        needsLayout = true;
        layoutChildren();
    }

    @Override
    protected void layoutChildren() {
        final int top = (int) snappedTopInset();
        final int right = (int) snappedRightInset();
        final int bottom = (int) snappedBottomInset();
        final int left = (int) snappedLeftInset();
        final int width = (int) getWidth() - left - right;
        final int height = (int) getHeight() - top - bottom;
        final double spacing = gridSpacing.get().doubleValue();

        canvas.setLayoutX(left + 1);
        canvas.setLayoutY(top + 1);

        if (width != canvas.getWidth() || height != canvas.getHeight() || needsLayout) {
            double zoom = zoomProperty.get();
            canvas.setWidth(width * zoom);
            canvas.setHeight(height * zoom);

            GraphicsContext g = canvas.getGraphicsContext2D();
            g.clearRect(0, 0, width * zoom, height * zoom);
            //g.setFill(gridColor.get());
            g.setLineWidth(1);
            g.setLineDashes(4);

            final int hLineCount = (int) Math.floor((height + 1) / spacing) - 1;
            final int vLineCount = (int) Math.floor((width + 1) / spacing) - 1;
            for (int i = 0; i < hLineCount; i++) {
                g.strokeLine(0, snap((i + 1) * spacing * zoom), width * zoom, snap((i + 1) * spacing * zoom));
            }

            for (int i = 0; i < vLineCount; i++) {
                g.strokeLine(snap((i + 1) * spacing * zoom), 0, snap((i + 1) * spacing * zoom), height * zoom);
            }

            needsLayout = false;
        }
    }

    private static double snap(double y) {
        return ((int) y) + HALF_PIXEL_OFFSET;
    }

    /**
     * @return The CssMetaData associated with this class, including the
     * CssMetaData of its super classes.
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    private static class StyleableProperties {

        private static final CssMetaData<ResizableGrid, Paint> GRID_COLOR = new CssMetaData<ResizableGrid, Paint>(
                "-graph-grid-color", PaintConverter.getInstance()) {

            @Override
            public boolean isSettable(ResizableGrid node) {
                return !node.gridColor.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(ResizableGrid node) {
                return node.gridColor;
            }
        };

        private static final CssMetaData<ResizableGrid, Number> GRID_SPACING = new CssMetaData<ResizableGrid, Number>(
                "-graph-grid-spacing", SizeConverter.getInstance()) {

            @Override
            public boolean isSettable(ResizableGrid node) {
                return !node.gridSpacing.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(ResizableGrid node) {
                return node.gridSpacing;
            }
        };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(
                    Pane.getClassCssMetaData());
            styleables.add(GRID_COLOR);
            styleables.add(GRID_SPACING);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }
}
