package org.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.RadioButton;
//import javafx.scene.control.RadioMenuItemBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
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
import org.gui.messages.Messages;
import org.gui.options.Option;

import static org.gui.messages.Constants.ABOUT_ITEM;
import static org.gui.messages.Constants.ABOUT_TITLE;
import static org.gui.messages.Constants.CHOOSE_LANGUAGE;
import static org.gui.messages.Constants.EXIT_ITEM;
import static org.gui.messages.Constants.FILE_MENU;
import static org.gui.messages.Constants.HELP_MENU;
import static org.gui.messages.Constants.LANGUAGE_ITEM;
import static org.gui.messages.Constants.LOAD_ITEM;
import static org.gui.messages.Constants.PLUGIN_MENU;

public class AppMenu extends MenuBar {

	private final App app;

	private Stage aboutStage;

	private Image image;
	private int imageHeight;
	private int imageWidth;
	private ImageView imageView;

	private String localUrl;

	public AppMenu(App app, final Stage stage) {

		super();
		this.app = app;
		Menu fileMenu = new Menu(app.getMessages().getString(FILE_MENU));
		Menu pluginMenu = new Menu(app.getMessages().getString(PLUGIN_MENU));
		Menu helpMenu = new Menu(app.getMessages().getString(HELP_MENU));

		final FileChooser fc = new FileChooser();

		aboutStage = new Stage();

		Option option = app.getOption();
		aboutStage.setTitle(app.getMessages().getString(ABOUT_TITLE));

		aboutStage.setHeight(option.height() * 0.4);
		aboutStage.setWidth(option.width() * 0.8);
		aboutStage.setScene(new Scene(new Group(new Label("Описание базового плагина iMage"))));

		MenuItem loadItem = new MenuItem(app.getMessages().getString(LOAD_ITEM));
		MenuItem exitItem = new MenuItem(app.getMessages().getString(EXIT_ITEM));
		MenuItem aboutItem = new MenuItem(app.getMessages().getString(ABOUT_ITEM));
		MenuItem languageItem = new MenuItem(app.getMessages().getString(LANGUAGE_ITEM));
//		ObservableList<String> langs = FXCollections.observableArrayList("One", "Two", "Three");
//		ComboBox<String> comboBox = new ComboBox<>(langs);

		Label lbl = new Label();
		RadioMenuItem radioItem1 = new RadioMenuItem("English");
		RadioMenuItem radioItem2 = new RadioMenuItem("German");
		RadioMenuItem radioItem3 = new RadioMenuItem("French");

		ToggleGroup toggleGroup = new ToggleGroup();
		EventHandler<ActionEvent> actionEventEventHandler = actionEvent -> {
			RadioMenuItem radioMenuItem = (RadioMenuItem)actionEvent.getSource();
			switch (radioMenuItem.getText()){
				case "English" : App.setLocale(Locale.ENGLISH); break;
				case "German" : App.setLocale(Locale.GERMAN); break;
				case "French" : App.setLocale(Locale.FRENCH); break;
				default:
					App.setLocale(Locale.ENGLISH);
			}
			System.out.println(radioMenuItem.getText());
		};

		radioItem1.setOnAction(actionEventEventHandler);
		radioItem1.setToggleGroup(toggleGroup);

		radioItem2.setOnAction(actionEventEventHandler);
		radioItem2.setToggleGroup(toggleGroup);

		radioItem3.setOnAction(actionEventEventHandler);
		radioItem3.setToggleGroup(toggleGroup);

		Menu menu = new Menu("Selection");
		menu.getItems().addAll(radioItem1, radioItem2,radioItem3);

		MenuItem aboutItem1 = new MenuItem(app.getMessages().getString(ABOUT_ITEM));

		fileMenu.getItems().add(loadItem);
		fileMenu.getItems().add(exitItem);
		helpMenu.getItems().add(aboutItem);

		EventHandler<ActionEvent> openAboutDialogEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				aboutStage.show();
			}
		};

/*		EventHandler<ActionEvent> openLanguagesMenu = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				stage.show();
			}
		};*/

		EventHandler<ActionEvent> closeEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				stage.close();
			}
		};

		EventHandler<ActionEvent> selectFileEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {

				File selectedFile = fc.showOpenDialog(stage);
				localUrl = null;
				try {
					localUrl = selectedFile.toURI().toURL().toString();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
				imageWidth = 200;
				imageHeight = 200;
				PreviewImage previewImage = new PreviewImage(stage, localUrl, imageWidth, imageHeight, false, false);

			}

		};



		aboutItem.setOnAction(openAboutDialogEvent);
		exitItem.setOnAction(closeEvent);
		loadItem.setOnAction(selectFileEvent);


		this.getMenus().add(fileMenu);
		this.getMenus().add(menu);
		this.getMenus().add(helpMenu);
	}
}