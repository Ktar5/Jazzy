package com.ktar5.mapeditor.gui.centerview;

import com.ktar5.mapeditor.gui.utils.ResizableGrid;
import com.ktar5.mapeditor.gui.utils.ZoomablePannablePane;
import com.ktar5.mapeditor.util.ClipUtil;
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

        zoomablePannablePane = new ZoomablePannablePane(viewport);

        resizableGrid = new ResizableGrid(zoomablePannablePane.getPanAndZoomPane(), zoomablePannablePane.getZoomProperty());
        resizableGrid.setPrefSize(viewport.getPrefWidth(), viewport.getPrefHeight());

        this.getChildren().addAll(zoomablePannablePane, resizableGrid);

        ClipUtil.clip(this);
    }

    public EditorPane(Pair<Integer, Integer> dimensions) {
        this(dimensions.getKey(), dimensions.getValue());
    }

}
