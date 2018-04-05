package com.ktar5.mapeditor.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class WholeOrComposite {

    public static <T> Class<? extends T> getType(Class<? extends T> whole, Class<? extends T> composite) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Alert");
        alert.setContentText("Please select Composite or Whole");

        ButtonType compositeButton = new ButtonType("_Composite");
        ButtonType wholeButton = new ButtonType("_Whole");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(compositeButton, wholeButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == compositeButton) {
            return composite;
        } else if (result.get() == wholeButton) {
            return whole;
        }

        new GenericAlert("You didn't select a tilemap type");
        return null;
    }

}
