package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        TextAnimationPane pane = new TextAnimationPane();

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setScene(new Scene(pane, 750, 400));
        primaryStage.setTitle("Animation & Audio Looping");
        primaryStage.show();
    }

    private class TextAnimationPane extends BorderPane {

        Button btnStartAnimation = new Button("Play");
        Button btnLoop = new Button("Loop");
        Button btnStop = new Button("Stop");

        long animationSpeed;
        int imageCount;
        AudioClip audioClip;
        int currentImg = 1;
        String audioFileUrl;
        String imgPrefix;
        String imgDirectory = "/image/";
        String imgExtension = ".jpeg";

        Timeline timeline = null;

        StackPane centerPane = new StackPane();

        TextAnimationPane() {

            setCenter(centerPane);
            HBox topPane = new HBox(btnStartAnimation, btnLoop, btnStop);
            topPane.setAlignment(Pos.CENTER_RIGHT);
            topPane.setSpacing(3);
            setTop(topPane);

            GridPane bottomPane = new GridPane();
            bottomPane.setPadding(new Insets(15));
            bottomPane.setHgap(15);

            Label lblInfo = new Label("Enter information for animation");
            bottomPane.add(lblInfo, 0, 0);

            TextField txtfAnimationSpeed = new TextField();
            txtfAnimationSpeed.setPrefColumnCount(45);
            Label lblAnimationSpeed = new Label("Animation speed in milliseconds");
            bottomPane.add(lblAnimationSpeed, 0, 1);
            bottomPane.add(txtfAnimationSpeed, 1, 1);

            TextField txtfImagePrefix = new TextField();
            txtfImagePrefix.setPrefColumnCount(45);
            Label lblImagePrefix = new Label("Image file prefix");
            bottomPane.add(lblImagePrefix, 0, 2);
            bottomPane.add(txtfImagePrefix, 1, 2);

            TextField txtfNumberOfImages = new TextField();
            txtfNumberOfImages.setPrefColumnCount(45);
            Label lblNumberOfImages = new Label("Number of Images");
            bottomPane.add(lblNumberOfImages, 0, 3);
            bottomPane.add(txtfNumberOfImages, 1, 3);

            TextField txtfAudioFileUrl = new TextField();
            txtfAudioFileUrl.setPrefColumnCount(45);
            Label lblAudioFileUrl = new Label("Audio file URL");
            bottomPane.add(lblAudioFileUrl, 0, 4);
            bottomPane.add(txtfAudioFileUrl, 1, 4);

            setBottom(bottomPane);

            btnStartAnimation.setOnAction(e -> {
                animationSpeed = Long.parseLong(txtfAnimationSpeed.getText().trim());
                imgPrefix = txtfImagePrefix.getText().trim();
                imageCount = Integer.parseInt(txtfNumberOfImages.getText());
                audioFileUrl = txtfAudioFileUrl.getText();


                AudioClip audioClip = new AudioClip(audioFileUrl);
                if (audioClip.isPlaying()) {
                    audioClip.stop();
                }
                audioClip.setCycleCount(1);
                audioClip.play();
                timelineStart("Start");
            });

            btnLoop.setOnAction(e -> {
                animationSpeed = Long.parseLong(txtfAnimationSpeed.getText().trim());
                imgPrefix = txtfImagePrefix.getText().trim();
                imageCount = Integer.parseInt(txtfNumberOfImages.getText());
                audioFileUrl = txtfAudioFileUrl.getText();


                AudioClip audioClip = new AudioClip(audioFileUrl);
                if (audioClip.isPlaying()) {
                    audioClip.stop();
                }
                audioClip.setCycleCount(AudioClip.INDEFINITE);
                audioClip.play();
                timelineLoop();
            });

            btnStop.setOnAction(e -> {
                timelineStart("Stop");
                AudioClip audioClip = new AudioClip(audioFileUrl);
                audioClip.stop();
            });

        }


        private void timelineStart(String lineType) {
            if (timeline != null) {
                timeline.stop();
                timeline = null;
                currentImg = 1;
            }

            if (lineType == "Stop") {
                return;
            }

            timeline = new Timeline(
                    new KeyFrame(Duration.millis(animationSpeed), e -> nextImage()));
            timeline.setCycleCount(imageCount);
            timeline.setOnFinished(event -> audioClip.stop());
            timeline.play();
        }

        private void timelineLoop() {
            if (timeline != null) {
                timeline.stop();
                timeline = null;
                currentImg = 1;
            }

            timeline = new Timeline(
                    new KeyFrame(Duration.millis(animationSpeed), e -> nextImage()));
            timeline.setCycleCount(imageCount);
            timeline.setOnFinished(event -> timelineLoop());
            timeline.play();

        }


        private void nextImage() {
            centerPane.getChildren().clear();
            centerPane.getChildren().add(
                    new ImageView(new Image(imgDirectory + imgPrefix + currentImg++ + imgExtension)));
        }


    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}