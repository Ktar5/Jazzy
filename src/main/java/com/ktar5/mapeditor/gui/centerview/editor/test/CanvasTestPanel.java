package com.ktar5.mapeditor.gui.centerview.editor.test;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CanvasTestPanel extends JPanel {
    private final Consumer<Graphics> consumer;

    public CanvasTestPanel(int width, int height, Consumer<Graphics> consumer) {
        super();
        this.consumer = consumer;
        setPreferredSize(new Dimension(width, height));
        this.setVisible(true);
        this.setSize(new Dimension(width, height));
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawRect(20, 20, 20, 20);
        consumer.accept(g);
    }

}
