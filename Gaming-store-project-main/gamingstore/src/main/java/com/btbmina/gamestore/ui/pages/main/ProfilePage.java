package com.btbmina.gamestore.ui.pages.main;

import javax.swing.*;
import java.awt.*;
import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.components.PurpleButton;

public class ProfilePage extends JPanel {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;

    public ProfilePage() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_BACKGROUND);  // Use DARK_BACKGROUND from ColorScheme

        createUI();
    }

    private void createUI() {
        // Profile header
        JPanel headerPanel = createHeaderPanel();

        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.DARK_BACKGROUND);  // Use DARK_BACKGROUND for content panel

        // Profile info section
        JPanel profileSection = createProfileSection();

        // Statistics section
        JPanel statsSection = createStatsSection();

        // Recent activity section
        JPanel activitySection = createActivitySection();

        contentPanel.add(profileSection);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(statsSection);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(activitySection);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorScheme.PRIMARY_PURPLE);  // Use PRIMARY_PURPLE for header panel
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Profile picture
        JPanel avatarPanel = new JPanel();
        avatarPanel.setPreferredSize(new Dimension(100, 100));
        avatarPanel.setBackground(ColorScheme.MEDIUM_BACKGROUND);  // Use MEDIUM_BACKGROUND for avatar panel

        // Username
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        usernameLabel.setForeground(ColorScheme.TEXT_PRIMARY);  // Use TEXT_PRIMARY for text color

        panel.add(avatarPanel, BorderLayout.WEST);
        panel.add(usernameLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfileSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ColorScheme.MEDIUM_BACKGROUND);  // Use MEDIUM_BACKGROUND for profile section
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Profile fields
        usernameField = createProfileField("Username", "current_username");
        emailField = createProfileField("Email", "user@example.com");
        currentPasswordField = createPasswordField("Current Password");
        newPasswordField = createPasswordField("New Password");

        // Save button
        PurpleButton saveButton = new PurpleButton("Save Changes");
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.setMaximumSize(new Dimension(200, 40));

        panel.add(createSectionTitle("Profile Settings"));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(emailField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(currentPasswordField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(newPasswordField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(saveButton);

        return panel;
    }

    private JPanel createStatsSection() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 0));
        panel.setBackground(ColorScheme.DARK_BACKGROUND);  // Use DARK_BACKGROUND for stats section

        addStatCard(panel, "Games Owned", "42");
        addStatCard(panel, "Hours Played", "156");
        addStatCard(panel, "Achievements", "287");

        return panel;
    }

    private JPanel createActivitySection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ColorScheme.MEDIUM_BACKGROUND);  // Use MEDIUM_BACKGROUND for activity section
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(createSectionTitle("Recent Activity"));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add sample activities
        addActivity(panel, "Purchased Game X", "2 hours ago");
        addActivity(panel, "Achieved 'Master Player'", "5 hours ago");
        addActivity(panel, "Played Game Y", "Yesterday");

        return panel;
    }

    private JTextField createProfileField(String label, String defaultValue) {
        JTextField field = new JTextField(defaultValue);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createTitledBorder(label));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JPasswordField createPasswordField(String label) {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createTitledBorder(label));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setForeground(ColorScheme.TEXT_PRIMARY);  // Use TEXT_PRIMARY for section titles
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void addStatCard(JPanel container, String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(ColorScheme.MEDIUM_BACKGROUND);  // Use MEDIUM_BACKGROUND for stat cards
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(ColorScheme.TEXT_PRIMARY);  // Use TEXT_PRIMARY for value text
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);  // Use TEXT_PRIMARY for title text
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(titleLabel);

        container.add(card);
    }

    private void addActivity(JPanel container, String activity, String time) {
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBackground(ColorScheme.MEDIUM_BACKGROUND);  // Use MEDIUM_BACKGROUND for activity panel
        activityPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel activityLabel = new JLabel(activity);
        activityLabel.setForeground(ColorScheme.TEXT_PRIMARY);  // Use TEXT_PRIMARY for activity text

        JLabel timeLabel = new JLabel(time);
        timeLabel.setForeground(ColorScheme.TEXT_PRIMARY);  // Use TEXT_PRIMARY for time text

        activityPanel.add(activityLabel, BorderLayout.WEST);
        activityPanel.add(timeLabel, BorderLayout.EAST);

        container.add(activityPanel);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    // Add method to handle profile updates
    private void saveProfileChanges() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());

        // TODO: Implement profile update logic with database

        JOptionPane.showMessageDialog(this,
                "Profile updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
