package com.ktar5.jazzy.editor.gui.utils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ResizableGrid extends Pane {
    // This is to make the stroke be drawn 'on pixel'.
    private static final double HALF_PIXEL_OFFSET = -0.5;
    
    private final Canvas canvas = new Canvas();
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
    private boolean needsLayout;
    public final ObjectProperty<Paint> gridColor = new SimpleObjectProperty<Paint>(
            Color.rgb(0, 0, 0)) {
        
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
    
    public final DoubleProperty gridHorizontalSpacing = new DoublePropertyBase(16) {
        @Override
        public Object getBean() {
            return ResizableGrid.this;
        }
        
        @Override
        public String getName() {
            return "gridVerticalSpacing";
        }
        
        @Override
        protected void invalidated() {
            needsLayout = true;
            requestLayout();
        }
    };
    
    public final DoubleProperty gridVerticalSpacing = new DoublePropertyBase(16) {
        @Override
        public Object getBean() {
            return ResizableGrid.this;
        }
        
        @Override
        public String getName() {
            return "gridVerticalSpacing";
        }
        
        @Override
        protected void invalidated() {
            needsLayout = true;
            requestLayout();
        }
    };
    
    
    public ResizableGrid(Pane region, DoubleProperty regionZoomProperty, int spacingX, int spacingY) {
        this.gridHorizontalSpacing.set(spacingX);
        this.gridVerticalSpacing.set(spacingY);
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
    
    private static double snap(double y) {
        return ((int) y) + HALF_PIXEL_OFFSET;
    }
    
    @Override
    protected void layoutChildren() {
        final int top = (int) snappedTopInset();
        final int right = (int) snappedRightInset();
        final int bottom = (int) snappedBottomInset();
        final int left = (int) snappedLeftInset();
        final int width = (int) getWidth() - left - right;
        final int height = (int) getHeight() - top - bottom;
        final double spacingHorizontal = gridHorizontalSpacing.get();
        final double spacingVertical = gridVerticalSpacing.get();
        
        
        canvas.setLayoutX(left + 1);
        canvas.setLayoutY(top + 1);
        
        if (width != canvas.getWidth() || height != canvas.getHeight() || needsLayout) {
            double zoom = zoomProperty.get();
            canvas.setWidth(width * zoom);
            canvas.setHeight(height * zoom);
            
            GraphicsContext g = canvas.getGraphicsContext2D();
            g.clearRect(0, 0, width * zoom, height * zoom);
            g.setFill(gridColor.get());
            g.setLineWidth(1);
            g.setLineDashes(4);
            
            final int hLineCount = (int) Math.floor((height + 1) / spacingHorizontal) - 1;
            final int vLineCount = (int) Math.floor((width + 1) / spacingVertical) - 1;
            for (int i = 0; i < hLineCount; i++) {
                g.strokeLine(0, snap((i + 1) * spacingVertical * zoom), width * zoom, snap((i + 1) * spacingVertical * zoom));
            }
            
            for (int i = 0; i < vLineCount; i++) {
                g.strokeLine(snap((i + 1) * spacingHorizontal * zoom), 0, snap((i + 1) * spacingHorizontal * zoom), height * zoom);
            }
            
            needsLayout = false;
        }
    }
    
}
