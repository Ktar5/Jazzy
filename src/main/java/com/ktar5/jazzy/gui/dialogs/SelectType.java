package com.ktar5.jazzy.gui.dialogs;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.StringConverter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class SelectType<T> extends Dialog<Class<? extends T>> {
    private final GridPane grid;
    private final Label label;
    private final ComboBox<Class<? extends T>> comboBox;
    private final Class<? extends T> defaultChoice;
    
    public SelectType(Collection<Class<? extends T>> choices) {
        final DialogPane dialogPane = getDialogPane();
        
        // -- grid
        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);
        
        // -- label
        label = createLabel(dialogPane.getContentText());
        label.setPrefWidth(Region.USE_COMPUTED_SIZE);
        label.textProperty().bind(dialogPane.contentTextProperty());
        
        dialogPane.contentTextProperty().addListener(o -> updateGrid());
        
        setTitle(ControlResources.getString("Dialog.confirm.title"));
        dialogPane.setHeaderText(ControlResources.getString("Dialog.confirm.header"));
        dialogPane.getStyleClass().add("choice-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        final double MIN_WIDTH = 150;
        
        // -- initialization
        HashMap<String, Class<? extends T>> types = new HashMap<>();
        for (Class<? extends T> clazz : choices) {
            types.put(clazz.getSimpleName(), clazz);
        }
        
        // -- combo box
        ObservableList<Class<? extends T>> observableList = FXCollections.observableArrayList(choices);
        comboBox = new ComboBox<>(observableList);
        comboBox.setConverter(new StringConverter<Class<? extends T>>() {
            @Override
            public String toString(Class<? extends T> object) {
                return object.getSimpleName();
            }
            
            @Override
            public Class<? extends T> fromString(String string) {
                return types.get(string);
            }
        });
        comboBox.setMinWidth(MIN_WIDTH);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        
        // -- display
        GridPane.setHgrow(comboBox, Priority.ALWAYS);
        GridPane.setFillWidth(comboBox, true);
        
        // -- default choice
        defaultChoice = choices.iterator().next();
        if (defaultChoice == null) {
            comboBox.getSelectionModel().selectFirst();
        } else {
            comboBox.getSelectionModel().select(defaultChoice);
        }
        
        // -- more display
        updateGrid();
        
        // -- return / result
        setResultConverter((dialogButton) -> {
            ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonData.OK_DONE ? comboBox.getSelectionModel().getSelectedItem() : null;
        });
    }
    
    public static <Z> Class<? extends Z> getType(Class<? extends Z>... options) {
        return getType(Arrays.asList(options));
    }
    
    public static <Z> Class<? extends Z> getType(Collection<Class<? extends Z>> options) {
        final SelectType<Z> zSelectType = new SelectType<>(options);
        return zSelectType.showAndWait().get();
    }
    
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.getStyleClass().add("content");
        label.setWrapText(true);
        label.setPrefWidth(360);
        return label;
    }
    
    private void updateGrid() {
        grid.getChildren().clear();
        
        grid.add(label, 0, 0);
        grid.add(comboBox, 1, 0);
        getDialogPane().setContent(grid);
        
        Platform.runLater(comboBox::requestFocus);
    }
    
}

