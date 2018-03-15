package com.ktar5.mapeditor.gui.utils;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {

    public NumberTextField(String promptText) {
        super();
        this.setPromptText(promptText);
    }

    public NumberTextField(String promptText, int defaultNumber) {
        this(promptText);
        this.setText(String.valueOf(defaultNumber));
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (validate(text)) {
            super.replaceSelection(text);
        }
    }

    public int getNumber() {
        return Integer.valueOf(this.getText());
    }

    private boolean validate(String text) {
        return text.matches("[0-9]*");
    }
}
