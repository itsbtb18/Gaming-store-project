package com.btbmina.gamestore.animation;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Manages animations throughout the application
 * Provides methods for fade, slide, and transition effects
 */
public class AnimationManager {
    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);

    /**
     * Fade in a component
     *
     * @param component The component to fade in
     * @param duration Duration in milliseconds
     * @param delay Delay before starting in milliseconds
     * @param callback Optional callback to run after animation
     */
    public static void fadeIn(JComponent component, int duration, int delay, Runnable callback) {
        component.setOpaque(false);
        float[] opacity = {0f};

        // Set initial opacity
        component.setAlpha(opacity[0]);
        component.setVisible(true);

        int steps = Math.max(duration / 16, 1); // ~60fps
        float increment = 1.0f / steps;

        Runnable fadeTask = new Runnable() {
            private int step = 0;

            @Override
            public void run() {
                opacity[0] += increment;
                if (opacity[0] > 1f) opacity[0] = 1f;

                SwingUtilities.invokeLater(() -> {
                    component.setAlpha(opacity[0]);
                    component.repaint();
                });

                step++;
                if (step >= steps) {
                    if (callback != null) {
                        SwingUtilities.invokeLater(callback);
                    }
                    ((SwingWorker)this).cancel(true);
                }
            }
        };

        // Schedule the animation
        scheduler.scheduleAtFixedRate(
                fadeTask, delay, duration / steps, TimeUnit.MILLISECONDS);
    }

    /**
     * Fade out a component
     *
     * @param component The component to fade out
     * @param duration Duration in milliseconds
     * @param delay Delay before starting in milliseconds
     * @param callback Optional callback to run after animation
     */
    public static void fadeOut(JComponent component, int duration, int delay, Runnable callback) {
        float[] opacity = {1f};

        int steps = Math.max(duration / 16, 1); // ~60fps
        float decrement = 1.0f / steps;

        Runnable fadeTask = new Runnable() {
            private int step = 0;

            @Override
            public void run() {
                opacity[0] -= decrement;
                if (opacity[0] < 0f) opacity[0] = 0f;

                SwingUtilities.invokeLater(() -> {
                    component.setAlpha(opacity[0]);
                    component.repaint();
                });

                step++;
                if (step >= steps) {
                    SwingUtilities.invokeLater(() -> {
                        if (opacity[0] <= 0) {
                            component.setVisible(false);
                        }
                        if (callback != null) {
                            callback.run();
                        }
                    });
                    ((SwingWorker)this).cancel(true);
                }
            }
        };

        // Schedule the animation
        scheduler.scheduleAtFixedRate(
                fadeTask, delay, duration / steps, TimeUnit.MILLISECONDS);
    }

    /**
     * Slide in a component from a specific direction
     *
     * @param component The component to slide in
     * @param direction Direction to slide from (TOP, BOTTOM, LEFT, RIGHT)
     * @param duration Duration in milliseconds
     * @param delay Delay before starting in milliseconds
     * @param callback Optional callback to run after animation
     */
    public static void slideIn(JComponent component, int direction, int duration, int delay, Runnable callback) {
        // Implementation details would depend on specific requirements
        // This would need to be customized based on your layout manager and container
    }

    /**
     * Slide out a component in a specific direction
     *
     * @param component The component to slide out
     * @param direction Direction to slide to (TOP, BOTTOM, LEFT, RIGHT)
     * @param duration Duration in milliseconds
     * @param delay Delay before starting in milliseconds
     * @param callback Optional callback to run after animation
     */
    public static void slideOut(JComponent component, int direction, int duration, int delay, Runnable callback) {
        // Implementation details would depend on specific requirements
        // This would need to be customized based on your layout manager and container
    }

    /**
     * Transition between two frames
     *
     * @param currentFrame The current frame to transition from
     * @param nextFrame The next frame to transition to
     * @param type The type of transition (FADE, SLIDE_LEFT, SLIDE_RIGHT, etc.)
     * @param duration Duration in milliseconds
     */
    public static void transitionFrames(JFrame currentFrame, JFrame nextFrame, int type, int duration) {
        // Implementation for frame transitions
        // This would handle seamless transitions between different main windows
    }

    /**
     * Shutdown the animation scheduler
     */
    public static void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    // Extension of JComponent with alpha capability for fade effects
    public static abstract class AlphaContainer extends JComponent {
        private float alpha = 1.0f;

        public void setAlpha(float alpha) {
            this.alpha = alpha;
            repaint();
        }

        public float getAlpha() {
            return alpha;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.paintComponent(g2d);
            g2d.dispose();
        }
    }
}