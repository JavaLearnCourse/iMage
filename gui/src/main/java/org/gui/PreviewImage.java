package org.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.gui.options.Option;

import static org.gui.messages.Constants.CANCEL_BTN;
import static org.gui.messages.Constants.CHANGING_TITLE;
import static org.gui.messages.Constants.SAVE_BTN;

public class PreviewImage {



    private Image image;
    private  ImageView imageView;
    private final FlowPane fp = new FlowPane();

    private final Slider sizeSlider = new Slider(100.0, 500.0, 200.0);
    private final Slider brightnessSlider = new Slider(-1.0, 1.0, 0.0);



    final Button saveButton = new Button(App.getMessages().getString(SAVE_BTN));

    final Button cancelButton = new Button(App.getMessages().getString(CANCEL_BTN));

    private Stage changeStage = new Stage();
    private Stage stage;
    private String url;
    private int height;
    private  int width;


    public PreviewImage(Stage stage,  String url, int width, int height, boolean ratio, boolean smooth){
        this.image = new Image(url, width, height, ratio, smooth );
        this.stage = stage;
        changeStage.setTitle(App.getMessages().getString(CHANGING_TITLE));
        this.url = url;
        this.imageView = new ImageView(image);
        this.width = width;
        this.height = height;


        EventHandler<ActionEvent> cancelEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                changeStage.close();
            }
        };
        EventHandler<ActionEvent> saveEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                new Paint(stage, image, width, height);
                changeStage.close();
            }
        };
        saveButton.setOnAction(saveEvent);
        cancelButton.setOnAction(cancelEvent);
        saveButton.getStyleClass().add("save-btn");
        sizeSlider.valueProperty().addListener(new SizeSliderListener());
        brightnessSlider.valueProperty().addListener(new BrightnessSliderLiistener());
        fp.getChildren().addAll(sizeSlider, brightnessSlider, saveButton, cancelButton);
        fp.setPadding(new Insets(20));
        BorderPane bp = new BorderPane(imageView, null, null, fp, null);
        VBox box = new VBox( bp);
        Scene scene = new Scene(box, Option.width(), Option.height());
        changeStage.setScene(scene);
        changeStage.show();

    }

    private class SizeSliderListener implements ChangeListener<Number> {

        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            int  imageHeight = newValue.intValue();
            int  imageWidth = newValue.intValue();
            imageView.setFitHeight(imageHeight);
            imageView.setFitWidth(imageWidth);
            BorderPane bp = new BorderPane(imageView, null, null, fp, null);
            VBox box = new VBox(bp);
            Scene scene = new Scene(box, Option.width(), Option.height());
            changeStage.setScene(scene);
            changeStage.show();

        }

    }

    private class  BrightnessSliderLiistener implements   ChangeListener<Number>{
        public void changed(ObservableValue<? extends Number> observable, //
                            Number oldValue, Number newValue) {

            Image image1 = new Image(url, image.getHeight(), image.getWidth(), false, false);
            int width = (int) image1.getWidth();
            int height = (int) image1.getHeight();
            double shift = newValue.doubleValue();
            WritableImage wImage = new WritableImage(width, height);

            PixelReader pixelReader = image1.getPixelReader();

            PixelWriter writer = wImage.getPixelWriter();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    Color color = pixelReader.getColor(x, y);
                    Color color1;
                    if(shift >= 0.0) {
                        color1 = new Color(Math.min((color.getRed() + shift), 1.0), Math.min((color.getGreen() + shift), 1.0), Math.min((color.getBlue() + shift), 1.0), 1.0);
                    }
                    else {
                        color1 = new Color(Math.max((color.getRed() + shift), 0.0), Math.max((color.getGreen() + shift), 0.0), Math.max((color.getBlue() + shift), 0.0), 1.0);
                    }


                    writer.setColor(x, y, color1);
                }
            }

            image = wImage;

            imageView = new ImageView(image);
            imageView.setFitHeight(height);
            imageView.setFitWidth(width);
            BorderPane bp = new BorderPane(imageView, null, null, fp, null);
            VBox box = new VBox(bp);
            Scene scene = new Scene(box, Option.width(), Option.height());
            changeStage.setScene(scene);
            changeStage.show();

        }
    }
}
