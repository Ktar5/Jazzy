package com.ktar5.mapeditor.gui.centerview.editor.tabs;

import com.ktar5.mapeditor.gui.centerview.editor.EditorCanvas;
import com.ktar5.mapeditor.gui.centerview.editor.EditorPane;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.stage.StageStyle;
import lombok.Getter;
import org.pmw.tinylog.Logger;

import java.util.Optional;
import java.util.UUID;

@Getter
public abstract class EditorTab extends Tab {
    private UUID uuid;
    private boolean hasEdits = false;

    public EditorTab(UUID uuid) {
        this.uuid = uuid;
        this.setText(getName());
        this.setContent(new EditorPane(getCanvas()));
        this.setOnCloseRequest(e -> {
            if (this.hasEdits) {
                newSaveConfirmation(e);
            }
        });
        this.setOnClosed(e -> removeCurrent());
        draw();
    }

    public abstract void draw();

    abstract EditorCanvas getCanvas();

    abstract String getName();

    abstract void saveCurrent();

    abstract void removeCurrent();

    public void setEdit(boolean value) {
        if (value == hasEdits) {
            return;
        }
        hasEdits = value;
        if (hasEdits && !this.getText().startsWith("* ")) {
            this.setText("* " + this.getText());
        } else {
            this.setText(getName());
        }
    }

    public void newSaveConfirmation(Event event) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initStyle(StageStyle.UTILITY);

        alert.setTitle("Quit Without Saving");
        alert.setContentText("Are you sure you'd like to quit without saving changes to " +
                getName() + "?");


        ButtonType closeNoSave = new ButtonType("Close without saving");
        ButtonType saveAndClose = new ButtonType("Save and close");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getDialogPane().setPrefWidth(500);
        alert.getButtonTypes().setAll(closeNoSave, saveAndClose, cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == closeNoSave) {
            //Do nothing
        } else if (result.get() == saveAndClose) {
            saveCurrent();
        } else if (result.get() == cancel) {
            event.consume();
        } else {
            Logger.info("Something happened over here");
            event.consume();
        }
    }

}
