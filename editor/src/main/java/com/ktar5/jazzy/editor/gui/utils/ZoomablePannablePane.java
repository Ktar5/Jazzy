package com.ktar5.jazzy.editor.gui.utils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Getter;

@Getter
public class ZoomablePannablePane extends AnchorPane {
    private static final double[] ZOOM_LEVELS = {
            0.015625, 0.03125, 0.0625, 0.125, 0.25, 0.33, 0.5,
            0.75, 1.0, 1.5, 2.0, 3.0, 4.0, 5.5, 8.0, 11.0, 16.0,
            23.0, 32.0, 45.0, 64.0, 90.0, 128.0, 180.0, 256.0
    };
    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(1.0d);
    private final DoubleProperty deltaY = new SimpleDoubleProperty(0.0d);
    private final Group group = new Group();
    int zoomLevel = 8;
    private PanAndZoomPane panAndZoomPane;
    
    public ZoomablePannablePane(Node node) {
        //this.setPannable(true);
        group.getChildren().add(node);
        
        // create canvas
        panAndZoomPane = new PanAndZoomPane();
        zoomProperty.bind(panAndZoomPane.myScale);
        deltaY.bind(panAndZoomPane.deltaY);
        panAndZoomPane.getChildren().add(group);
        
        SceneGestures sceneGestures = new SceneGestures(panAndZoomPane);
        
        panAndZoomPane.toBack();
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, sceneGestures.getOnMouseClickedEventHandler());
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        this.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        this.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
        
        this.getChildren().add(panAndZoomPane);
    }
    
    public class PanAndZoomPane extends Pane {
        
        public static final double DEFAULT_DELTA = 1.3d;
        public DoubleProperty deltaY = new SimpleDoubleProperty(0.0);
        DoubleProperty myScale = new SimpleDoubleProperty(1.0);
        
        public PanAndZoomPane() {
            // add scale transform
            scaleXProperty().bind(myScale);
            scaleYProperty().bind(myScale);
        }
        
        
        public double getScale() {
            return myScale.get();
        }
        
        public void setScale(double scale) {
            myScale.set(scale);
        }
        
        public void setPivot(double x, double y, double scale) {
            // note: pivot value must be untransformed, i. e. without scaling
            setTranslateX((int) getTranslateX() - x);
            setTranslateY((int) getTranslateY() - y);
            myScale.set(scale);
        }
        
        /**
         * !!!! The problem is in this method  !!!!
         * <p>
         * The calculations are incorrect, and result in unpredictable behavior
         */
        public void fitWidth() {
            double scale = getParent().getLayoutBounds().getMaxX() / getLayoutBounds().getMaxX();
            double oldScale = getScale();
            
            double f = (scale / oldScale) - 1;
            
            double dx = getTranslateX() - getBoundsInParent().getMinX() - getBoundsInParent().getWidth() / 2;
            double dy = getTranslateY() - getBoundsInParent().getMinY() - getBoundsInParent().getHeight() / 2;
            
            double newX = f * dx + getBoundsInParent().getMinX();
            double newY = f * dy + getBoundsInParent().getMinY();
            
            setPivot(newX, newY, scale);
        }
        
        public void resetZoom() {
            double scale = 1.0d;
            
            double x = getTranslateX();
            double y = getTranslateY();
            
            setPivot(x, y, scale);
        }
        
        public double getDeltaY() {
            return deltaY.get();
        }
        
        public void setDeltaY(double dY) {
            deltaY.set(dY);
        }
    }
    
    
    /**
     * Mouse drag context used for scene and nodes.
     */
    class DragContext {
        
        double mouseAnchorX;
        double mouseAnchorY;
        
        double translateAnchorX;
        double translateAnchorY;
        
    }
    
    /**
     * Listeners for making the scene's canvas draggable and zoomable
     */
    public class SceneGestures {
        
        PanAndZoomPane panAndZoomPane;
        private DragContext sceneDragContext = new DragContext();
        private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (!event.isControlDown() && event.getButton() == MouseButton.MIDDLE) {
                    sceneDragContext.mouseAnchorX = event.getX();
                    sceneDragContext.mouseAnchorY = event.getY();
                    
                    sceneDragContext.translateAnchorX = panAndZoomPane.getTranslateX();
                    sceneDragContext.translateAnchorY = panAndZoomPane.getTranslateY();
                }
            }
        };
        private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (!event.isControlDown() && event.getButton() == MouseButton.MIDDLE) {
                    panAndZoomPane.setTranslateX(sceneDragContext.translateAnchorX + event.getX() - sceneDragContext.mouseAnchorX);
                    panAndZoomPane.setTranslateY(sceneDragContext.translateAnchorY + event.getY() - sceneDragContext.mouseAnchorY);
                    
                    event.consume();
                }
            }
        };
        /**
         * Mouse wheel handler: zoom to pivot point
         */
        private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {
            
            @Override
            public void handle(ScrollEvent event) {
                
                if (!event.isControlDown()) {
                    event.consume();
                    return;
                }
                
                double delta = PanAndZoomPane.DEFAULT_DELTA;
                
                double scale = panAndZoomPane.getScale(); // currently we only use Y, same value is used for X
                double oldScale = scale;
                
                zoomLevel += event.getDeltaY() < 0 ? -1 : 1;
                if (scale >= ZOOM_LEVELS.length) {
                    zoomLevel -= 1;
                } else if (scale <= 0) {
                    zoomLevel = 0;
                }
                scale = ZOOM_LEVELS[zoomLevel];
                
                panAndZoomPane.setDeltaY(event.getDeltaY());
                /*
                if (panAndZoomPane.deltaY.get() < 0) {
                    scale = scale / delta < 1.0 ? 1.0 : scale / delta;
                } else {
                    scale = scale * delta > 10 ? 10 : scale * delta;
                }*/
                
                double f = (scale / oldScale) - 1;
                
                double dx = (event.getX() - (panAndZoomPane.getBoundsInParent().getWidth() / 2 + panAndZoomPane.getBoundsInParent().getMinX()));
                double dy = (event.getY() - (panAndZoomPane.getBoundsInParent().getHeight() / 2 + panAndZoomPane.getBoundsInParent().getMinY()));
                
                panAndZoomPane.setPivot(f * dx, f * dy, scale);
                System.out.println(scale);
                event.consume();
            }
        };
        /**
         * Mouse click handler
         */
        private EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2 && event.isControlDown()) {
                        panAndZoomPane.resetZoom();
                    }
                }
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    if (event.getClickCount() == 2 && event.isControlDown()) {
                        panAndZoomPane.fitWidth();
                    }
                }
            }
        };
        
        public SceneGestures(PanAndZoomPane canvas) {
            this.panAndZoomPane = canvas;
        }
        
        public EventHandler<MouseEvent> getOnMouseClickedEventHandler() {
            return onMouseClickedEventHandler;
        }
        
        public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
            return onMousePressedEventHandler;
        }
        
        public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
            return onMouseDraggedEventHandler;
        }
        
        public EventHandler<ScrollEvent> getOnScrollEventHandler() {
            return onScrollEventHandler;
        }
    }
}
