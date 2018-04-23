package com.ktar5.jazzy.editor.gui;

import com.ktar5.jazzy.editor.gui.bottombar.BottomBar;
import com.ktar5.jazzy.editor.gui.centerview.TabHoldingPane;
import com.ktar5.jazzy.editor.gui.topmenu.TopMenu;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

@Getter
public class Root extends BorderPane {
    private TopMenu topMenu;
    
    private TabHoldingPane centerView;
    
    private BottomBar bottomBar;
    
    public Root() {
        super();
        this.setTop(topMenu = new TopMenu());
        this.setCenter(centerView = new TabHoldingPane());
        this.setBottom(bottomBar = new BottomBar());
    }
    
}
