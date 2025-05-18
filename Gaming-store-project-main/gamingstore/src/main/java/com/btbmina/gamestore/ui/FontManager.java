package com.btbmina.gamestore.ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private static final Map<String, Font> fontCache = new HashMap<>();

    private static final String FONT_PATH = "/assets/fonts/";
    private static final String FONT_REGULAR = "Inter-Regular.ttf";
    private static final String FONT_BOLD = "Inter-Bold.ttf";
    private static final String FONT_MEDIUM = "Inter-Medium.ttf";
    private static final String FONT_LIGHT = "Inter-Light.ttf";
    private static final String FONT_TITLE = "Montserrat-Bold.ttf";

    public static final String FONT_NAME_REGULAR = "Inter Regular";
    public static final String FONT_NAME_BOLD = "Inter Bold";
    public static final String FONT_NAME_MEDIUM = "Inter Medium";
    public static final String FONT_NAME_LIGHT = "Inter Light";
    public static final String FONT_NAME_TITLE = "Montserrat Bold";

    static {
        loadFonts();
    }

    private static void loadFonts() {
        loadFont(FONT_REGULAR, FONT_NAME_REGULAR);
        loadFont(FONT_BOLD, FONT_NAME_BOLD);
        loadFont(FONT_MEDIUM, FONT_NAME_MEDIUM);
        loadFont(FONT_LIGHT, FONT_NAME_LIGHT);
        loadFont(FONT_TITLE, FONT_NAME_TITLE);
    }

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

    public static Font getFont(String fontName, int style, int size) {
        Font font = fontCache.get(fontName);
        if (font == null) {
            System.err.println("Font not loaded: " + fontName + ". Using default font.");
            return new Font("Segoe UI", style, size);
        }
        return font.deriveFont(style, size);
    }


    public static Font getRegular(int size) {
        return getFont(FONT_NAME_REGULAR, Font.PLAIN, size);
    }

    public static Font getBold(int size) {
        return getFont(FONT_NAME_BOLD, Font.BOLD, size);
    }

    public static Font getMedium(int size) {
        return getFont(FONT_NAME_MEDIUM, Font.PLAIN, size);
    }


    public static Font getLight(int size) {
        return getFont(FONT_NAME_LIGHT, Font.PLAIN, size);
    }


    public static Font getTitle(int size) {
        return getFont(FONT_NAME_TITLE, Font.BOLD, size);
    }
}