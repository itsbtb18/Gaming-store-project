package com.btbmina.gamestore.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Manages the application theme including colors, styles, and visual elements
 * Uses a purple-based color scheme to match the project requirements
 */
public class AppTheme {
    public static final Color PRIMARY_PURPLE = new Color(88, 55, 153);       // Main purple
    public static final Color PRIMARY_DARK = new Color(39, 32, 58);          // Dark background
    public static final Color SECONDARY_PURPLE = new Color(120, 81, 169);    // Secondary purple
    public static final Color ACCENT_PURPLE = new Color(149, 90, 231);       // Accent purple for highlights
    public static final Color LIGHT_PURPLE = new Color(187, 155, 235);       // Light purple for hover effects

    public static final Color TEXT_WHITE = new Color(245, 245, 245);         // Main text color
    public static final Color TEXT_LIGHT_GRAY = new Color(200, 200, 200);    // Secondary text
    public static final Color TEXT_DARK_GRAY = new Color(100, 100, 100);     // Disabled text

    public static final Color BG_DARK = new Color(25, 22, 33);              // Dark background
    public static final Color BG_MEDIUM = new Color(35, 30, 45);            // Medium background
    public static final Color BG_LIGHT = new Color(45, 40, 60);             // Light background

    public static final Color SUCCESS_GREEN = new Color(66, 186, 150);      // Success/positive actions
    public static final Color WARNING_ORANGE = new Color(237, 151, 74);     // Warnings/cautions
    public static final Color ERROR_RED = new Color(234, 81, 95);           // Errors/negative actions

    public static final int FONT_TITLE_LARGE = 32;
    public static final int FONT_TITLE = 24;
    public static final int FONT_SUBTITLE = 18;
    public static final int FONT_REGULAR = 14;
    public static final int FONT_SMALL = 12;

    public static Border BORDER_PURPLE = BorderFactory.createLineBorder(PRIMARY_PURPLE, 1);
    public static Border BORDER_DARK = BorderFactory.createLineBorder(BG_DARK, 1);
    public static Border BORDER_MEDIUM = BorderFactory.createLineBorder(BG_MEDIUM, 1);

    public static void applyPanelTheme(JPanel panel) {
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    public static void applyButtonTheme(JButton button) {
        button.setBackground(PRIMARY_PURPLE);
        button.setForeground(TEXT_WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, FONT_REGULAR));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    public static void applySecondaryButtonTheme(JButton button) {
        button.setBackground(BG_MEDIUM);
        button.setForeground(TEXT_LIGHT_GRAY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, FONT_REGULAR));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void applyLabelTheme(JLabel label) {
        label.setForeground(TEXT_WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, FONT_REGULAR));
    }

    public static void applyTitleTheme(JLabel label) {
        label.setForeground(TEXT_WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, FONT_TITLE));
    }

    public static void applyTextFieldTheme(JTextField textField) {
        textField.setBackground(BG_MEDIUM);
        textField.setForeground(TEXT_WHITE);
        textField.setCaretColor(TEXT_WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BORDER_MEDIUM,
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, FONT_REGULAR));
    }
}