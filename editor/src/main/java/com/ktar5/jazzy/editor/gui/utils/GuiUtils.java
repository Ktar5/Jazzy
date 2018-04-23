package com.ktar5.jazzy.editor.gui.utils;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public final class GuiUtils {
    
    public static void addListener(ChangeListener<? super String> listener, TextField... fields) {
        for (TextField field : fields) {
            field.textProperty().addListener(listener);
        }
    }
    
    public static <T> void forAll(Consumer<T> consumer, T... things) {
        for (T thing : things) {
            consumer.accept(thing);
        }
    }
    
    
}
