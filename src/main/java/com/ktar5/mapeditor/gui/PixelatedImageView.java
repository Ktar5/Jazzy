package com.ktar5.mapeditor.gui;

import com.sun.javafx.sg.prism.NGImageView;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseResourceFactory;
import javafx.scene.image.ImageView;

@SuppressWarnings("restriction")
public class PixelatedImageView extends ImageView {

    public PixelatedImageView(javafx.scene.image.Image image) {
        super(image);
    }

    @Override
    protected NGNode impl_createPeer() {
        return new NGImageView() {
            private Image image;

            @Override
            public void setImage(Object img) {
                super.setImage(img);
                image = (Image) img;
            }

            @Override
            protected void renderContent(Graphics g) {
                BaseResourceFactory factory = (BaseResourceFactory) g.getResourceFactory();
                Texture tex = factory.getCachedTexture(image, Texture.WrapMode.CLAMP_TO_EDGE);
                tex.setLinearFiltering(false);
                tex.unlock();
                super.renderContent(g);
            }
        };
    }


}

