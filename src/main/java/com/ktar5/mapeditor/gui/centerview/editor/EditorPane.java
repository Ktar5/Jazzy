package com.ktar5.mapeditor.gui.centerview.editor;

import com.ktar5.mapeditor.gui.centerview.editor.test.CanvasTestPanel;
import javafx.embed.swing.SwingNode;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import lombok.Getter;

import javax.swing.*;

@Getter
public class EditorPane extends ScrollPane {
    private EditorCanvas canvas;

    public EditorPane(SwingNode swingNode){
        super();
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.prefHeight(500);
        this.prefWidth(500);
        this.setContent(swingNode);
    }

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
            canvas.setInt();
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
