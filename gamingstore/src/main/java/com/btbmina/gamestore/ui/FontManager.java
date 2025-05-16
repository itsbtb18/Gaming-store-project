package com.btbmina.gamestore.ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages custom fonts for the application
 * Loads and provides access to professional fonts
 */
public class FontManager {
    private static final Map<String, Font> fontCache = new HashMap<>();

    // Font paths
    private static final String FONT_PATH = "/assets/fonts/";
    private static final String FONT_REGULAR = "Inter-Regular.ttf";
    private static final String FONT_BOLD = "Inter-Bold.ttf";
    private static final String FONT_MEDIUM = "Inter-Medium.ttf";
    private static final String FONT_LIGHT = "Inter-Light.ttf";
    private static final String FONT_TITLE = "Montserrat-Bold.ttf";

    // Font names
    public static final String FONT_NAME_REGULAR = "Inter Regular";
    public static final String FONT_NAME_BOLD = "Inter Bold";
    public static final String FONT_NAME_MEDIUM = "Inter Medium";
    public static final String FONT_NAME_LIGHT = "Inter Light";
    public static final String FONT_NAME_TITLE = "Montserrat Bold";

    static {
        loadFonts();
    }

    /**
     * Load all custom fonts into the system
     */
    private static void loadFonts() {
        loadFont(FONT_REGULAR, FONT_NAME_REGULAR);
        loadFont(FONT_BOLD, FONT_NAME_BOLD);
        loadFont(FONT_MEDIUM, FONT_NAME_MEDIUM);
        loadFont(FONT_LIGHT, FONT_NAME_LIGHT);
        loadFont(FONT_TITLE, FONT_NAME_TITLE);
    }

    /**
     * Load a single font file into the system
     */
    private static void loadFont(String fontFile, String fontName) {
        try {
            InputStream is = FontManager.class.getResourceAsStream(FONT_PATH + fontFile);
            if (is == null) {
                System.err.println("Font file not found: " + fontFile);
                return;
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            fontCache.put(fontName, font);

        } catch (IOException | FontFormatException e) {
            System.err.println("Error loading font: " + fontFile);
            e.printStackTrace();
        }
    }

    /**
     * Get a font with the specified style and size
     */
    public static Font getFont(String fontName, int style, int size) {
        Font font = fontCache.get(fontName);
        if (font == null) {
            System.err.println("Font not loaded: " + fontName + ". Using default font.");
            return new Font("Segoe UI", style, size);
        }
        return font.deriveFont(style, size);
    }

    /**
     * Get the regular font with the specified size
     */
    public static Font getRegular(int size) {
        return getFont(FONT_NAME_REGULAR, Font.PLAIN, size);
    }

    /**
     * Get the bold font with the specified size
     */
    public static Font getBold(int size) {
        return getFont(FONT_NAME_BOLD, Font.BOLD, size);
    }

    /**
     * Get the medium font with the specified size
     */
    public static Font getMedium(int size) {
        return getFont(FONT_NAME_MEDIUM, Font.PLAIN, size);
    }

    /**
     * Get the light font with the specified size
     */
    public static Font getLight(int size) {
        return getFont(FONT_NAME_LIGHT, Font.PLAIN, size);
    }

    /**
     * Get the title font with the specified size
     */
    public static Font getTitle(int size) {
        return getFont(FONT_NAME_TITLE, Font.BOLD, size);
    }
}