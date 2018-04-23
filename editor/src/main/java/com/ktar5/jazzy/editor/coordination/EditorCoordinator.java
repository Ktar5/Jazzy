package com.ktar5.jazzy.editor.coordination;

import com.ktar5.jazzy.editor.Main;
import com.ktar5.jazzy.editor.gui.centerview.TabHoldingPane;
import com.ktar5.jazzy.editor.gui.centerview.tabs.AbstractTab;

public class EditorCoordinator {
    private static EditorCoordinator instance;
    
    public EditorCoordinator() {
    
    
    }
    
    public static EditorCoordinator get() {
        if (instance == null) {
            instance = new EditorCoordinator();
        }
        return instance;
    }
    
    public AbstractTab getCurrentTab() {
        return getEditor().getCurrentTab();
    }
    
    public TabHoldingPane getEditor() {
        return Main.root.getCenterView();
    }
    
    
}
