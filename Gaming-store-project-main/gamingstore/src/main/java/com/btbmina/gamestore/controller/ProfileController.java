package com.btbmina.gamestore.controller;

import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.service.ProfileService;

import javax.swing.*;

public class ProfileController {
    private ProfileService profileService;
    private User currentUser;

    public ProfileController(User user) {
        this.currentUser = user;
        this.profileService = new ProfileService();
    }

    // Affiche les informations du profil dans des champs de texte
    public void loadProfile(JTextField usernameField, JTextField emailField) {
        User user = profileService.getUserProfile(currentUser.getUserId());
        if (user != null) {
            usernameField.setText(user.getUsername());
            emailField.setText(user.getEmail());
        } else {
            JOptionPane.showMessageDialog(null, "Impossible de charger le profil.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Met à jour les infos de profil (username, email)
    public void updateProfile(String newUsername, String newEmail, String currentPasswordInput) {
        if (!currentPasswordInput.equals(currentUser.getPassword())) {
            JOptionPane.showMessageDialog(null, "Mot de passe incorrect.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);

        boolean updated = profileService.updateUserProfile(currentUser);
        if (updated) {
            JOptionPane.showMessageDialog(null, "Profil mis à jour avec succès.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Échec de la mise à jour du profil.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Change le mot de passe
    public void changePassword(String oldPassword, String newPassword) {
        if (!oldPassword.equals(currentUser.getPassword())) {
            JOptionPane.showMessageDialog(null, "Ancien mot de passe incorrect.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean changed = profileService.changePassword(currentUser.getUserId(), newPassword);
        if (changed) {
            currentUser.setPassword(newPassword); // MAJ objet courant
            JOptionPane.showMessageDialog(null, "Mot de passe changé avec succès.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Échec du changement de mot de passe.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Supprime le profil utilisateur
    public void deleteProfile(JFrame frameToClose) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "Voulez-vous vraiment supprimer votre compte ? Cette action est irréversible.",
                "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = profileService.deleteUserProfile(currentUser.getUserId());
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Compte supprimé avec succès.",
                        "Supprimé", JOptionPane.INFORMATION_MESSAGE);
                frameToClose.dispose(); // Ferme la fenêtre actuelle
                // Ici tu peux aussi rediriger vers l'écran de login
            } else {
                JOptionPane.showMessageDialog(null, "Échec de la suppression du compte.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
