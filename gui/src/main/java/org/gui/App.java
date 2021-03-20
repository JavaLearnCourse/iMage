/*package org.gui;


public class App 
{
	
    public static void main( String[] args )
    {
    	MainFrame mf = new MainFrame();
        
    }
}*/
package org.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.gui.messages.Messages;
import org.gui.options.Option;
import org.gui.options.Theme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static org.gui.messages.Constants.TITLE_OF_FRAME;

public class App extends Application {

	private Option opt;
	private String currentTheme;
	private AppMenu menu;
	private static Locale locale = Locale.ENGLISH;

	public static void main(String[] args) {
		launch(args);
	}

	public App() {
		getInit();
		opt = new Option();
		currentTheme = opt.getTheme(Theme.STANDARD);
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle(getMessages().getString(TITLE_OF_FRAME));
		menu =  new AppMenu(this, stage);
		VBox vbox = new VBox(menu);
		Scene scene = new Scene(vbox, opt.width(), opt.height());
		scene.getStylesheets().add(currentTheme);
		stage.setScene(scene);
		stage.show();
	}

	public AppMenu getMenu() {
		return menu;
	}

	public void setLocale(Locale locale1) {

		try (FileWriter writer = new FileWriter("src/main/resouces/lang.properties", false)) {
			String text = locale1.toString();
			writer.write(text);
			writer.flush();
		} catch (IOException ex) {

		}
		locale = locale1;
	}

	public void getInit() {
		try (Scanner myReaderIt = new Scanner(new File("src/main/resouces/lang.properties"))) {
			List<String> linesIt = new ArrayList<>();
			while (myReaderIt.hasNextLine())
				linesIt.add(myReaderIt.nextLine());
			setLocale(new Locale(linesIt.get(0)));
		} catch (FileNotFoundException e) {
			setLocale(Locale.ENGLISH);
		}
	}

	public Locale getLocale() {
		return locale;
	}

	public Option getOption() {
		return opt;
	}

	public static Messages getMessages() {
		Messages mes = new Messages(locale);
		return mes;
	}

	public String getCurrentTheme() {
		return currentTheme;
	}
}