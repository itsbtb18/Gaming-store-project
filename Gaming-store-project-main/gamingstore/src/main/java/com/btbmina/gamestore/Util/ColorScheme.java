package com.btbmina.gamestore.Util;

import java.awt.Color;

public class ColorScheme {

    public static final Color PRIMARY_PURPLE = new Color(102, 0, 204);             // Deep Purple
    public static final Color SECONDARY_PURPLE = new Color(153, 51, 255);          // Lighter Purple
    public static final Color ACCENT_PINK = new Color(255, 105, 180);              // For highlights

    public static final Color DARK_PURPLE = new Color(49, 27, 80);                 // Dark Purple
    public static final Color ACCENT_COLOR = new Color(187, 134, 252);             // Accent Color

    public static final Color DARK_BACKGROUND = new Color(18, 18, 18);             // Almost black
    public static final Color MEDIUM_BACKGROUND = new Color(28, 28, 28);           // Card background
    public static final Color LIGHT_BACKGROUND = new Color(40, 40, 40);            // Hover effects

    public static final Color TEXT_PRIMARY = Color.BLACK;
    public static final Color TEXT_SECONDARY = new Color(130, 130, 130);
    public static final Color TEXT_DISABLED = new Color(130, 130, 130);

    public static final Color BORDER_COLOR = new Color(70, 70, 70);
    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 100);              // Transparent black

    public static final Color BUTTON_NORMAL = PRIMARY_PURPLE;
    public static final Color BUTTON_HOVER = new Color(156, 90, 240);              // Lighter purple
    public static final Color BUTTON_PRESSED = new Color(126, 70, 200);            // Darker purple

    public static final Color OVERLAY_DARK = new Color(0, 0, 0, 160);              // Semi-transparent dark
    public static final Color OVERLAY_WHITE = new Color(255, 255, 255, 30);        // Semi-transparent light

    public static final Color GRADIENT_TOP = new Color(102, 0, 204);
    public static final Color GRADIENT_BOTTOM = new Color(51, 0, 102);

    public static final Color SCROLLBAR_TRACK = new Color(30, 30, 30);
    public static final Color SCROLLBAR_THUMB = new Color(90, 90, 90);
    public static final Color SCROLLBAR_THUMB_HOVER = new Color(120, 120, 120);

    public static Color withAlpha(Color base, int alpha) {
        return new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha);
    }

    private ColorScheme() {

    }
}