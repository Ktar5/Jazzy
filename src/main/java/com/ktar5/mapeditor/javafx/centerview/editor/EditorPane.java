package com.ktar5.mapeditor.javafx.centerview.editor;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import org.pmw.tinylog.Logger;

@Getter
public class EditorPane extends ScrollPane {
    private EditorCanvas canvas;

    public EditorPane(EditorCanvas canvas) {
        super();

        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.prefHeight(-1);
        this.prefWidth(-1);

        this.canvas = canvas;
        this.setContent(canvas);

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
