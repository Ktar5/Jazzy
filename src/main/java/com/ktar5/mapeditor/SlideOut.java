package com.ktar5.mapeditor;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

/** Example of a sidebar that slides in and out of view */
public class SlideOut extends Application {
    private static final String[] lyrics = {
            "And in the end,\nthe love you take,\nis equal to\nthe love\nyou make.",
            "She came in through\nthe bathroom window\nprotected by\na silver\nspoon."
    };
    int lyricIndex = 0;
    public static void main(String[] args) throws Exception { launch(args); }
    public void start(final Stage stage) throws Exception {
        stage.setTitle("Slide out demo");

        // create some content to put in the sidebar.
        final Text lyric = new Text(lyrics[lyricIndex]);
        lyric.setFill(Color.DARKGREEN);
        lyric.setTextAlignment(TextAlignment.CENTER);
        lyric.setStyle("-fx-font-size: 20;");
        final Button changeLyric = new Button("New Lyrics");
        changeLyric.setMaxWidth(Double.MAX_VALUE);
        changeLyric.setOnAction(actionEvent -> {
            lyricIndex++;
            lyric.setText(lyrics[lyricIndex % 2]);
        });
        final BorderPane lyricPane = new BorderPane();
        lyricPane.setCenter(lyric);
        lyricPane.setBottom(changeLyric);

        // place the content in the sidebar.
        SideBar sidebar = new SideBar(lyricPane);
        VBox.setVgrow(lyricPane, Priority.ALWAYS);
        VBox.setVgrow(sidebar,   Priority.ALWAYS);
        sidebar.setMinWidth(250);

        // create a WebView to show to the right of the SideBar;
        WebView webView = new WebView();
        webView.getEngine().load("http://docs.oracle.com/javafx/2.0/get_started/jfxpub-get_started.htm");

        // layout the scene.
        final StackPane layout = new StackPane();
        layout.setStyle("-fx-background-color: cornsilk; -fx-font-size: 20; -fx-padding: 10;");
        layout.getChildren().addAll(
                HBoxBuilder.create().spacing(10)
                        .children(
                                VBoxBuilder.create().spacing(10)
                                        .children(
                                                sidebar.getControlButton(),
                                                sidebar
                                        ).build(),
                                webView
                        ).build()
        );
        Scene scene = new Scene(layout);
        webView.prefWidthProperty().bind(scene.widthProperty());
        webView.prefHeightProperty().bind(scene.heightProperty());
        stage.setScene(scene);
        stage.show();
    }

    /** Animates a node on and off screen to the left. */
    class SideBar extends VBox {
        /** @return a control button to hide and show the sidebar */
        public Button getControlButton() { return controlButton; }
        private final Button controlButton;

        /** creates a sidebar containing a vertical alignment of the given nodes */
        SideBar(Node... nodes) {
            // create a bar to hide and show.
            setAlignment(Pos.CENTER);
            setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to bottom, lavenderblush, mistyrose); -fx-border-color: derive(mistyrose, -10%); -fx-border-width: 3;");
            getChildren().addAll(nodes);

            // create a button to hide and show the sidebar.
            controlButton = new Button("Hide");
            controlButton.setMaxWidth(Double.MAX_VALUE);
            controlButton.setTooltip(new Tooltip("Play sidebar hide and seek"));

            // apply the animations when the button is pressed.
            controlButton.setOnAction(actionEvent -> {
                // create an animation to hide sidebar.
                final double startWidth = getWidth();
                final Animation hideSidebar = new Transition() {
                    { setCycleDuration(Duration.millis(250)); }
                    protected void interpolate(double frac) {
                        final double curWidth = startWidth * (1.0 - frac);
                        setTranslateX(-startWidth + curWidth);
                    }
                };
                hideSidebar.onFinishedProperty().set(actionEvent12 -> {
                    setVisible(false);
                    controlButton.setText("Show");
                });

                // create an animation to show a sidebar.
                final Animation showSidebar = new Transition() {
                    { setCycleDuration(Duration.millis(250)); }
                    protected void interpolate(double frac) {
                        final double curWidth = startWidth * frac;
                        setTranslateX(-startWidth + curWidth);
                    }
                };
                showSidebar.onFinishedProperty().set(actionEvent1 -> controlButton.setText("Hide"));

                if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
                    if (isVisible()) {
                        hideSidebar.play();
                    } else {
                        setVisible(true);
                        showSidebar.play();
                    }
                }
            });
        }
    }

}