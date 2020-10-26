package org.gui;

import org.gui.instruments.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Paint {

	public Paint(final Stage stage, Image image, int imageWidth, int imageHeight) {

		Button rubberButton = new Button("Rubber");
		Button pencilButton = new Button("Pencil");
		Button textButton = new Button("Text");
		Button saveButton = new Button("Save");
		TextArea textArea = new TextArea();
		
		Parent menu = stage.getScene().getRoot();
		
		FlowPane fpDraw = new FlowPane();
		fpDraw.getChildren().addAll(rubberButton, pencilButton, textButton, saveButton);
		
		
		
		VBox vb = new VBox();
		

		Canvas canvas = new Canvas(600, 600);
		final GraphicsContext gc;
		gc = canvas.getGraphicsContext2D();
		gc.drawImage(image, 0, 0, (double) imageWidth, (double) imageHeight);
		BorderPane bp = new BorderPane(canvas, fpDraw, null, null, null);
		vb.getChildren().addAll(menu, bp);
		stage.setScene(new Scene(vb, 700, 700));
		stage.show();

		EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
			   InstrumentContainer c =  Context.getContext();
			   c.drawRubber(gc,canvas, image);
			}
		};

		EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				InstrumentContainer c =  Context.getContext();
				c.drawPencil(gc, canvas, image);
			}
		};
		
		EventHandler<ActionEvent> event3 = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Text text = new Text();
				text.drawText(gc, canvas, textArea);
			}
		};
		
		EventHandler<ActionEvent> event4 = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				  FileChooser savefile = new FileChooser();
		            savefile.setTitle("Save File");
		            
		            File file = savefile.showSaveDialog(stage);
		            if (file != null) {
		                try {
		                    WritableImage writableImage = new WritableImage(imageWidth, imageHeight);
		                    canvas.snapshot(null, writableImage);
		                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
		                    ImageIO.write(renderedImage, "png", file);
		                } catch (IOException ex) {
		                    System.out.println("Error!");
		                }
		            }
			}
		};

		rubberButton.setOnAction(event1);
		pencilButton.setOnAction(event2);
		textButton.setOnAction(event3);
		saveButton.setOnAction(event4);
	}
}
