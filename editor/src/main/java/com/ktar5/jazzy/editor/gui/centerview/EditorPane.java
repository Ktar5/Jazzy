package com.ktar5.jazzy.editor.gui.centerview;

import com.ktar5.jazzy.editor.gui.utils.ResizableGrid;
import com.ktar5.jazzy.editor.gui.utils.ZoomablePannablePane;
import com.ktar5.jazzy.editor.util.ClipUtil;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Pair;
import lombok.Getter;

@Getter
public class EditorPane extends Pane {
    private final ZoomablePannablePane zoomablePannablePane;
    private Pane viewport;
    private ResizableGrid resizableGrid;
    
    public EditorPane(int x, int y, int gridXSpacing, int gridYSpacing) {
        super();
        
        viewport = new Pane();
        viewport.setMaxSize(x, y);
        viewport.setPrefSize(x, y);
        
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(this, Priority.ALWAYS);
        
        zoomablePannablePane = new ZoomablePannablePane(viewport);
        
        resizableGrid = new ResizableGrid(zoomablePannablePane.getPanAndZoomPane(), zoomablePannablePane.getZoomProperty(),
                gridXSpacing, gridYSpacing);
        resizableGrid.setPrefSize(viewport.getPrefWidth(), viewport.getPrefHeight());
        
        this.getChildren().addAll(zoomablePannablePane, resizableGrid);
        
        ClipUtil.clip(this);
    }
    
    public EditorPane(Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> dimensions) {
        this(dimensions.getKey().getKey(), dimensions.getKey().getValue(),
                dimensions.getValue().getKey(), dimensions.getValue().getValue());
    }
    
}
