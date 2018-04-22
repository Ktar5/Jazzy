package com.ktar5.jazzy.gui.dialogs;

import com.ktar5.jazzy.util.Tabbable;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;
import org.pmw.tinylog.Logger;

import java.util.Optional;

public class QuitSaveConfirmation {
    
    public static void quitSaveConfirmation(Event event, Tabbable tabbable) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initStyle(StageStyle.UTILITY);
        
        alert.setTitle("Quit Without Saving");
        alert.setContentText("Are you sure you'd like to quit without saving changes to " +
                tabbable.getName() + "?");
        
        
        ButtonType closeNoSave = new ButtonType("Close without saving");
        ButtonType saveAndClose = new ButtonType("Save and close");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getDialogPane().setPrefWidth(500);
        alert.getButtonTypes().setAll(closeNoSave, saveAndClose, cancel);
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == closeNoSave) {
            //Do nothing
        } else if (result.get() == saveAndClose) {
            tabbable.save();
        } else if (result.get() == cancel) {
            event.consume();
        } else {
            Logger.info("Something happened over here");
            event.consume();
        }
    }
    
    
}
