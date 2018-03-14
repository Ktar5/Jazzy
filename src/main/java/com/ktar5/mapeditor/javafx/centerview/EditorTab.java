package com.ktar5.mapeditor.javafx.centerview;

import com.ktar5.mapeditor.tilemap.MapManager;
import com.ktar5.mapeditor.tilemap.Tilemap;
import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.stage.StageStyle;
import lombok.Getter;
import org.pmw.tinylog.Logger;

import java.util.Optional;
import java.util.UUID;

@Getter
public class EditorTab extends Tab {
    private UUID tilemap;
    private boolean hasEdits;

    public EditorTab(UUID tilemap) {
        Tilemap map = MapManager.get().getMap(tilemap);
        this.tilemap = map.getId();
        this.setText(map.getMapName());
        this.setContent(new EditorPane(map.getCanvas()));
        this.setOnCloseRequest(e -> {
            if (isHasEdits()) {
                newSaveConfirmation(e);
                if(!e.isConsumed()){
                    MapManager.get().remove(tilemap);
                }
            }
        });
    }

    public void newSaveConfirmation(Event event) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initStyle(StageStyle.UTILITY);

        alert.setTitle("Quit Without Saving");
        //alert.setHeaderText("");
        alert.setContentText("Are you sure you'd like to quit without saving changes to " +
                MapManager.get().getMap(tilemap).getMapName() + "?");


        ButtonType closeNoSave = new ButtonType("Close without saving");
        ButtonType saveAndClose = new ButtonType("Save and close");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getDialogPane().setPrefWidth(500);
        alert.getButtonTypes().setAll(closeNoSave, saveAndClose, cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == closeNoSave) {
            MapManager.get().remove(tilemap);
        } else if (result.get() == saveAndClose) {
            MapManager.get().saveMap(tilemap);
            MapManager.get().remove(tilemap);
        } else if (result.get() == cancel) {
            event.consume();
        } else {
            Logger.info("Something happened over here");
            event.consume();
        }
    }

    public void setEdit(boolean value) {
        if(value == hasEdits){
            return;
        }
        hasEdits = value;
        if (hasEdits && !this.getText().startsWith("* ")) {
            this.setText("* " + this.getText());
        } else {
            this.setText(MapManager.get().getMap(tilemap).getMapName());
        }
    }

    @Getter
    public class EditorPane extends ScrollPane {

        public EditorPane(Canvas canvas) {
            super();

            this.setFitToHeight(true);
            this.setFitToWidth(true);
            this.prefHeight(-1);
            this.prefWidth(-1);

            this.setContent(canvas);
        }
    }

}
