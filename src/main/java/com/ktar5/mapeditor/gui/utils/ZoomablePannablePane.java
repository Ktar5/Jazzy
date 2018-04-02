package com.ktar5.mapeditor.gui.utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ZoomablePannablePane extends ScrollPane {

    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(1.0d);
    private final DoubleProperty deltaY = new SimpleDoubleProperty(0.0d);

    private final Group group = new Group();

    public AnchorPane set(Node node) {
        //this.setPannable(true);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);

        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setBottomAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);

        AnchorPane root = new AnchorPane();

        root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        group.getChildren().add(node);

        // create canvas
        PanAndZoomPane panAndZoomPane = new PanAndZoomPane();
        zoomProperty.bind(panAndZoomPane.myScale);
        deltaY.bind(panAndZoomPane.deltaY);
        panAndZoomPane.getChildren().add(group);

        SceneGestures sceneGestures = new SceneGestures(panAndZoomPane);

        this.setContent(panAndZoomPane);
        panAndZoomPane.toBack();
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, sceneGestures.getOnMouseClickedEventHandler());
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        this.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        this.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        root.getChildren().add(this);
        return root;
    }

    class PanAndZoomPane extends Pane {

        public static final double DEFAULT_DELTA = 1.3d;
        DoubleProperty myScale = new SimpleDoubleProperty(1.0);
        public DoubleProperty deltaY = new SimpleDoubleProperty(0.0);
        private Timeline timeline;


        public PanAndZoomPane() {

            this.timeline = new Timeline(60);

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
            // timeline that scales and moves the node
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.millis(50), new KeyValue(translateXProperty(), getTranslateX() - x)),
                    new KeyFrame(Duration.millis(50), new KeyValue(translateYProperty(), getTranslateY() - y)),
                    new KeyFrame(Duration.millis(50), new KeyValue(myScale, scale))
            );
            timeline.play();

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

        private DragContext sceneDragContext = new DragContext();

        PanAndZoomPane panAndZoomPane;

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

                panAndZoomPane.setDeltaY(event.getDeltaY());
                if (panAndZoomPane.deltaY.get() < 0) {
                    scale /= delta;
                } else {
                    scale *= delta;
                }

                double f = (scale / oldScale) - 1;

                double dx = (event.getX() - (panAndZoomPane.getBoundsInParent().getWidth() / 2 + panAndZoomPane.getBoundsInParent().getMinX()));
                double dy = (event.getY() - (panAndZoomPane.getBoundsInParent().getHeight() / 2 + panAndZoomPane.getBoundsInParent().getMinY()));

                panAndZoomPane.setPivot(f * dx, f * dy, scale);

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
    }
}
