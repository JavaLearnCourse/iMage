package org.gui.options;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class contains configuration. It reads property from a file placed in resources folder.
 * @author  Dmitry Savkin
 * @version  1.0
 */
public final class Option {


    /**
     * Creates object for configuration of program.
     */
    public Option() {


    }

    private  static String getProp(final String key) {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "app.properties";
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appProps.getProperty(key);
    }

    /**
     * Width of main window
     * @return current width from property file
     */
    public static int width() {
        try {
            return  Integer.parseInt(getProp("width"));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Height of main window
     * @return  current height from property file
     */
    public static int height() {
        try {
            return  Integer.parseInt(getProp("height"));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static String getTheme(final String theme) {
        return "theme/" + theme + "/style.css";
    }
}
