package com.ktar5.mapeditor.gui.centerview.editor;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.Getter;

@Getter
public class EditorPane extends ScrollPane {
    private EditorCanvas canvas;
    private Pane pane = new Pane();

    public EditorPane(EditorCanvas canvas) {
        super();

        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.prefHeight(-1);
        this.prefWidth(-1);

        //pane.setVisible(true);

        this.canvas = canvas;
        this.setContent(canvas);
        this.getChildren().add(canvas);

        //Canvas drag
        this.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            //Logger.debug("End dragging");
            canvas.isDragging = false;
        });

        addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (!canvas.isDragging) {
                //Logger.debug("Set canvas initial drag values");
                canvas.pressedX = event.getSceneX();
                canvas.pressedY = event.getSceneY();
                canvas.origX = canvas.getTranslateX();
                canvas.origY = canvas.getTranslateY();
                canvas.isDragging = true;
            }
            //Logger.debug("Perform drag on canvas");
            canvas.setTranslateX(canvas.origX + (event.getSceneX() - canvas.pressedX));
            canvas.setTranslateY(canvas.origY + (event.getSceneY() - canvas.pressedY));

            event.consume();
        });

    }
}
