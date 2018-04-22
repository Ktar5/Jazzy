package com.ktar5.jazzy.gui;

import com.ktar5.jazzy.gui.bottombar.BottomBar;
import com.ktar5.jazzy.gui.centerview.TabHoldingPane;
import com.ktar5.jazzy.gui.topmenu.TopMenu;
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
