package org.gui.messages;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {


    private static final long serialVersionUID = -3129495340668876130L;

    private final String   BUNDLE_NAME = "language";

    private ResourceBundle RESOURCE_BUNDLE;

    /**
     * @param l
     *          the locale to use the correct resource bundle
     */
    public Messages(Locale l) {
        RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, l);
    }

    /**
     * @param key
     *          the message key
     * @return returns the message of the specified language
     */
    public String getString(String key)
    {
        try
        {
            return RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e)
        {
            e.printStackTrace();
            return key;
        }
    }
}
