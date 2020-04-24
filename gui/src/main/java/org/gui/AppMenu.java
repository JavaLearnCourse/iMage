package org.gui;

import java.io.File;
import java.net.MalformedURLException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.gui.options.Option;


import static org.gui.messages.Constants.FILE_MENU;
import static org.gui.messages.Constants.HELP_MENU;
import static org.gui.messages.Constants.LOAD_ITEM;
import static org.gui.messages.Constants.EXIT_ITEM;
import static org.gui.messages.Constants.ABOUT_ITEM;
import static org.gui.messages.Constants.CHANGING_TITLE;
import static org.gui.messages.Constants.ABOUT_TITLE;

public class AppMenu extends MenuBar {


	private final App app;

	private Stage aboutStage;
	private Stage changeStage;
	private Image image;
	private int imageHeight;
	private int imageWidth;
	private ImageView imageView;
	private File selectedFile;
	private String localUrl;

	public AppMenu(App app, final Stage stage) {

		super();
		this.app = app;
		Menu fileMenu = new Menu(app.getMessages().getString(FILE_MENU));
		Menu helpMenu = new Menu(app.getMessages().getString(HELP_MENU));

		final FileChooser fc = new FileChooser();

		aboutStage = new Stage();
		changeStage = new Stage();

		final FlowPane fp = new FlowPane();
		Option option = app.getOption();
		aboutStage.setTitle(app.getMessages().getString(ABOUT_TITLE));
		changeStage.setTitle(app.getMessages().getString(CHANGING_TITLE));
		aboutStage.setHeight(option.height() * 0.4);
		aboutStage.setWidth(option.width() * 0.8);
		aboutStage.setScene(new Scene(new Group(new Label("Описание базового плагина iMage"))));

		MenuItem loadItem = new MenuItem(app.getMessages().getString(LOAD_ITEM));		
		MenuItem exitItem = new MenuItem(app.getMessages().getString(EXIT_ITEM));
		MenuItem aboutItem = new MenuItem(app.getMessages().getString(ABOUT_ITEM));

		final Button saveButton = new Button("Save");
		saveButton.getStyleClass().add("save-btn");
		final Button cancelButton = new Button("Cancel");

		final Slider sizeSlider = new Slider(100.0, 500.0, 200.0);
		final Slider brightnessSlider = new Slider(-1.0, 1.0, 0.0);

		fileMenu.getItems().add(loadItem);
		fileMenu.getItems().add(exitItem);
		helpMenu.getItems().add(aboutItem);

		EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				aboutStage.show();
			}
		};

		EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				stage.close();
			}
		};

		EventHandler<ActionEvent> event3 = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {

				selectedFile = fc.showOpenDialog(stage);
				localUrl = null;
				try {
					localUrl = selectedFile.toURI().toURL().toString();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
				imageWidth = 200;
				imageHeight = 200;
				image = new Image(localUrl, imageWidth, imageHeight, false, false);

				imageView = new ImageView(image);
				fp.getChildren().addAll(sizeSlider, brightnessSlider, saveButton, cancelButton);
				fp.setPadding(new Insets(20));
				BorderPane bp = new BorderPane(imageView, null, null, fp, null);
				VBox box = new VBox( bp);
				Scene scene = new Scene(box, option.width(), option.height());

				changeStage.setScene(scene);
				changeStage.show();

				sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
					public void changed(ObservableValue<? extends Number> observable, //
							Number oldValue, Number newValue) {

						
						imageHeight = newValue.intValue();
						imageWidth = newValue.intValue();
						imageView.setFitHeight(imageHeight);
						imageView.setFitWidth(imageWidth);
						BorderPane bp = new BorderPane(imageView, null, null, fp, null);
						VBox box = new VBox(bp);
						Scene scene = new Scene(box, option.width(), option.height());
						changeStage.setScene(scene);
						changeStage.show();

					}
				});

				brightnessSlider.valueProperty().addListener(new ChangeListener<Number>() {/////////////////////////////////
					public void changed(ObservableValue<? extends Number> observable, //
							Number oldValue, Number newValue) {

						Image image1 = new Image(localUrl, image.getHeight(), image.getWidth(), false, false);
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
						imageView.setFitHeight(imageHeight);
						imageView.setFitWidth(imageWidth);
						BorderPane bp = new BorderPane(imageView, null, null, fp, null);
						VBox box = new VBox(app.getMenu(), bp);
						Scene scene = new Scene(box, option.width(), option.height());

						changeStage.setScene(scene);
						changeStage.show();

					}
				});

				EventHandler<ActionEvent> event5 = new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
					
						new Paint(stage, image, imageWidth, imageHeight);
						changeStage.close();
					}
				};
				saveButton.setOnAction(event5);
			}

		};

		/*
		 * EventHandler<ActionEvent> event5 = new EventHandler<ActionEvent>() { public
		 * void handle(ActionEvent e) { File dest = new
		 * File("C:\\Users\\User\\eclipse-workspace\\testmavenproject\\gui\\src\\main",
		 * selectedFile.getName()); InputStream is = null; OutputStream os = null; try {
		 * try { is = new FileInputStream(selectedFile); } catch (FileNotFoundException
		 * e1) { e1.printStackTrace(); } try { os = new FileOutputStream(dest); } catch
		 * (FileNotFoundException e1) { e1.printStackTrace(); } byte[] buffer = new
		 * byte[1024]; int length; try { while ((length = is.read(buffer)) > 0) {
		 * os.write(buffer, 0, length); } } catch (IOException e1) {
		 * e1.printStackTrace(); } } finally { try { is.close(); } catch (IOException
		 * e1) { e1.printStackTrace(); } try { os.close(); } catch (IOException e1) {
		 * e1.printStackTrace(); } } } };
		 */

		EventHandler<ActionEvent> event4 = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				changeStage.close();
			}
		};

		aboutItem.setOnAction(event1);
		exitItem.setOnAction(event2);
		loadItem.setOnAction(event3);
		cancelButton.setOnAction(event4);

		this.getMenus().add(fileMenu);
		this.getMenus().add(helpMenu);

	}

}
