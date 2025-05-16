package com.btbmina.gamestore.animation;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.btbmina.gamestore.animation.AnimationManager.AlphaContainer;

public class FadeEffect {
    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);

    // Fade in effect
    public static void fadeIn(JComponent component, int duration, int delay, Runnable callback) {
        if (component instanceof AlphaContainer) {
            AlphaContainer alphaComponent = (AlphaContainer) component;
            alphaComponent.setAlpha(0f);  // Start from fully transparent
            component.setVisible(true);
        } else {
            component.setOpaque(false);
        }

        final float[] opacity = {0f};
        int steps = Math.max(duration / 16, 1);  // ~60fps
        float increment = 1.0f / steps;

        final int interval = duration / steps;

        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(new Runnable() {
            int step = 0;

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
                    // Cancel the task
                    throw new RuntimeException("STOP");
                }
            }
        }, delay, interval, TimeUnit.MILLISECONDS);

        scheduler.schedule(() -> task.cancel(true), duration + delay + 100, TimeUnit.MILLISECONDS);
    }

    // Fade out effect
    public static void fadeOut(JComponent component, int duration, int delay, Runnable callback) {
        if (component instanceof AlphaContainer) {
            AlphaContainer alphaComponent = (AlphaContainer) component;
            alphaComponent.setAlpha(1f);  // Start from fully opaque
        } else {
            component.setOpaque(false);
        }

        final float[] opacity = {1f};
        int steps = Math.max(duration / 16, 1);  // ~60fps
        float decrement = 1.0f / steps;

        final int interval = duration / steps;

        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(new Runnable() {
            int step = 0;

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
                        if (opacity[0] <= 0f) {
                            component.setVisible(false);
                        }
                        if (callback != null) {
                            callback.run();
                        }
                    });
                    // Cancel the task
                    throw new RuntimeException("STOP");
                }
            }
        }, delay, interval, TimeUnit.MILLISECONDS);

        scheduler.schedule(() -> task.cancel(true), duration + delay + 100, TimeUnit.MILLISECONDS);
    }

    // Shutdown the animation scheduler
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
}
