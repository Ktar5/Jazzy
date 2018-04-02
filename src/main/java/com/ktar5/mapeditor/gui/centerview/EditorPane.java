package com.ktar5.mapeditor.gui.centerview;

import com.ktar5.mapeditor.gui.utils.ResizableGrid;
import com.ktar5.mapeditor.gui.utils.ZoomablePannablePane;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import lombok.Getter;

@Getter
public class EditorPane extends Pane {
    private Pane viewport;

    public EditorPane(int x, int y) {
        super();

        viewport = new Pane();
        viewport.setMaxSize(x, y);
        viewport.setPrefSize(x, y);

        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(this, Priority.ALWAYS);

        viewport.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        AnchorPane set = new ZoomablePannablePane().set(viewport);

        set.prefHeightProperty().bind(this.heightProperty());
        set.prefWidthProperty().bind(this.widthProperty());

        ResizableGrid resizableGrid = new ResizableGrid();
        resizableGrid.prefWidthProperty().bind(this.widthProperty());
        resizableGrid.prefHeightProperty().bind(this.heightProperty());

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
