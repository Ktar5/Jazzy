package com.ktar5.mapeditor.gui.centerview;

import com.ktar5.mapeditor.gui.utils.ResizableGrid;
import com.ktar5.mapeditor.gui.utils.ZoomablePannablePane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import lombok.Getter;

@Getter
public class EditorPane extends Pane {
    private Pane viewport;
    private ResizableGrid resizableGrid;
    private final ZoomablePannablePane zoomablePannablePane;

    public EditorPane(int x, int y) {
        super();

        viewport = new Pane();
        viewport.setMaxSize(x, y);
        viewport.setPrefSize(x, y);

        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(this, Priority.ALWAYS);

        //viewport.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        zoomablePannablePane = new ZoomablePannablePane();
        AnchorPane set = zoomablePannablePane.set(viewport);

        set.prefHeightProperty().bind(this.heightProperty());
        set.prefWidthProperty().bind(this.widthProperty());

        resizableGrid = new ResizableGrid(zoomablePannablePane);
        resizableGrid.setPrefSize(viewport.getPrefWidth(), viewport.getPrefHeight());

        resizableGrid.setPickOnBounds(false);
        resizableGrid.setMouseTransparent(true);

        viewport.setVisible(true);

        this.getChildren().add(set);
        this.getChildren().add(resizableGrid);

        // create rectangle with sizes of pane,
        // don't need to set x and y explicitly
        // as positions of clip node are relative to parent node
        // (to pane in our case)
        Rectangle clipRect = new Rectangle(this.getWidth(), this.getHeight());

        // bind properties so height and width of rect
        // changes according pane's width and height
        clipRect.heightProperty().bind(this.heightProperty());
        clipRect.widthProperty().bind(this.widthProperty());

        // set rect as clip rect
        this.setClip(clipRect);
    }

    public EditorPane(Pair<Integer, Integer> dimensions) {
        this(dimensions.getKey(), dimensions.getValue());
    }

}
