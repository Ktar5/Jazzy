package com.ktar5.mapeditor.gui.centerview.editor.tabs;

import com.ktar5.mapeditor.gui.centerview.editor.EditorPane;
import com.ktar5.mapeditor.util.Tabbable;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import lombok.Getter;
import org.pmw.tinylog.Logger;

import java.util.Optional;
import java.util.UUID;

@Getter
public abstract class EditorTab extends Tab {
    private UUID uuid;
    private boolean hasEdits = false;
    private EditorPane pane;

    public EditorTab(UUID uuid) {
        this.uuid = uuid;
        this.setText(getTabbable().getName());
        this.setContent(pane = new EditorPane(getTabbable().getDimensions()));
        this.setOnCloseRequest(e -> {
            if (this.hasEdits) {
                newSaveConfirmation(e);
            }
        });
        pane.getViewport().addEventFilter(MouseEvent.MOUSE_CLICKED, getTabbable()::onClick);
        pane.getViewport().addEventFilter(MouseEvent.MOUSE_DRAGGED, getTabbable()::onDrag);
        this.setOnClosed(e -> getTabbable().remove());
    }

    public abstract void draw();

    public abstract Tabbable getTabbable();

    public Pane getViewport() {
        return pane.getViewport();
    }

    public void setEdit(boolean value) {
        if (value == hasEdits) {
            return;
        }
        hasEdits = value;
        if (hasEdits && !this.getText().startsWith("* ")) {
            this.setText("* " + this.getText());
        } else {
            this.setText(getTabbable().getName());
        }
    }

    public void newSaveConfirmation(Event event) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initStyle(StageStyle.UTILITY);

        alert.setTitle("Quit Without Saving");
        alert.setContentText("Are you sure you'd like to quit without saving changes to " +
                getTabbable().getName() + "?");


        ButtonType closeNoSave = new ButtonType("Close without saving");
        ButtonType saveAndClose = new ButtonType("Save and close");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getDialogPane().setPrefWidth(500);
        alert.getButtonTypes().setAll(closeNoSave, saveAndClose, cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == closeNoSave) {
            //Do nothing
        } else if (result.get() == saveAndClose) {
            getTabbable().save();
        } else if (result.get() == cancel) {
            event.consume();
        } else {
            Logger.info("Something happened over here");
            event.consume();
        }
    }

}
