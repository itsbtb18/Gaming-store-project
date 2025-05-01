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
        if (component instanceof AlphaContainer) {
            AlphaContainer alphaComponent = (AlphaContainer) component;
            alphaComponent.setAlpha(0f);
            component.setVisible(true);
        } else {
            component.setOpaque(false);
        }

        final float[] opacity = {0f};
        int steps = Math.max(duration / 16, 1); // ~60fps
        float increment = 1.0f / steps;

        Runnable fadeTask = new Runnable() {
            private int step = 0;

            @Override
            public void run() {
                opacity[0] += increment;
                if (opacity[0] > 1f) opacity[0] = 1f;

                SwingUtilities.invokeLater(() -> {
                    if (component instanceof AlphaContainer) {
                        ((AlphaContainer) component).setAlpha(opacity[0]);
                    } else {
                        component.repaint();
                    }
                });

                step++;
                if (step >= steps) {
                    if (callback != null) {
                        SwingUtilities.invokeLater(callback);
                    }
                    ((Runnable) this).run();
                }
            }
        };

        // Schedule the animation
        scheduler.scheduleAtFixedRate(fadeTask, delay, duration / steps, TimeUnit.MILLISECONDS);
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
        if (component instanceof AlphaContainer) {
            AlphaContainer alphaComponent = (AlphaContainer) component;
            alphaComponent.setAlpha(1f);
        } else {
            component.setOpaque(false);
        }

        final float[] opacity = {1f};
        int steps = Math.max(duration / 16, 1); // ~60fps
        float decrement = 1.0f / steps;

        Runnable fadeTask = new Runnable() {
            private int step = 0;

            @Override
            public void run() {
                opacity[0] -= decrement;
                if (opacity[0] < 0f) opacity[0] = 0f;

                SwingUtilities.invokeLater(() -> {
                    if (component instanceof AlphaContainer) {
                        ((AlphaContainer) component).setAlpha(opacity[0]);
                    } else {
                        component.repaint();
                    }
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
                }
            }
        };

        // Schedule the animation
        scheduler.scheduleAtFixedRate(fadeTask, delay, duration / steps, TimeUnit.MILLISECONDS);
    }

    /**
     * Slide in a component from a specific direction
     * This would need to be customized based on your layout manager and container
     */
    public static void slideIn(JComponent component, int direction, int duration, int delay, Runnable callback) {
        // Implementation depends on the specific layout manager.
    }

    /**
     * Slide out a component in a specific direction
     * This would need to be customized based on your layout manager and container
     */
    public static void slideOut(JComponent component, int direction, int duration, int delay, Runnable callback) {
        // Implementation depends on the specific layout manager.
    }

    /**
     * Transition between two frames
     * This would handle seamless transitions between different main windows
     */
    public static void transitionFrames(JFrame currentFrame, JFrame nextFrame, int type, int duration) {
        // Implementation for frame transitions (customize as needed)
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
    public static class AlphaContainer extends JComponent {
        private float alpha = 1.0f;
        private final JComponent wrapped;

        public AlphaContainer(JComponent wrapped) {
            this.wrapped = wrapped;
            setLayout(new BorderLayout());
            add(wrapped, BorderLayout.CENTER);
            setOpaque(false);
        }

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
