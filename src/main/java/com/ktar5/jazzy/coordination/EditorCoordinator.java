package com.ktar5.jazzy.coordination;

import com.ktar5.jazzy.Main;
import com.ktar5.jazzy.gui.centerview.TabHoldingPane;
import com.ktar5.jazzy.gui.centerview.tabs.AbstractTab;

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
