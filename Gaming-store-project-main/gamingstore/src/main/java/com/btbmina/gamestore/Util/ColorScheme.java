package com.btbmina.gamestore.Util;

import java.awt.Color;

public class ColorScheme {

    // Primary Theme Colors
    public static final Color PRIMARY_PURPLE = new Color(102, 0, 204);             // Deep Purple
    public static final Color SECONDARY_PURPLE = new Color(153, 51, 255);          // Lighter Purple
    public static final Color ACCENT_PURPLE = new Color(170, 71, 255);             // Accent Purple
    public static final Color ACCENT_PINK = new Color(255, 105, 180);              // For highlights

    // Additional Colors needed by GamePage
    public static final Color DARK_PURPLE = new Color(49, 27, 80);                 // Dark Purple
    public static final Color ACCENT_COLOR = new Color(187, 134, 252);             // Accent Color

    // Backgrounds
    public static final Color DARK_BACKGROUND = new Color(18, 18, 18);             // Almost black
    public static final Color MEDIUM_BACKGROUND = new Color(28, 28, 28);           // Card background
    public static final Color LIGHT_BACKGROUND = new Color(40, 40, 40);            // Hover effects

    // Text
    public static final Color TEXT_PRIMARY = Color.WHITE;
    public static final Color TEXT_SECONDARY = new Color(200, 200, 200);
    public static final Color TEXT_DISABLED = new Color(130, 130, 130);

    // Borders and Dividers
    public static final Color BORDER_COLOR = new Color(50, 50, 50);
    public static final Color DIVIDER_COLOR = new Color(60, 60, 60);

    // Button States
    public static final Color BUTTON_NORMAL = PRIMARY_PURPLE;
    public static final Color BUTTON_HOVER = SECONDARY_PURPLE;
    public static final Color BUTTON_PRESSED = new Color(85, 0, 170);              // Darker purple

    // Transparent Overlays
    public static final Color OVERLAY_DARK = new Color(0, 0, 0, 160);              // Semi-transparent dark
    public static final Color OVERLAY_LIGHT = new Color(255, 255, 255, 30);        // Semi-transparent light

    // Gradients (used in panels or animations)
    public static final Color GRADIENT_TOP = new Color(102, 0, 204);
    public static final Color GRADIENT_BOTTOM = new Color(153, 51, 255);

    private ColorScheme() {
        // Prevent instantiation
    }
}