package com.btbmina.gamestore.animation;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

public class TransitionEffect {
    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);

    public static void transition(JComponent fromComponent, JComponent toComponent, int duration, int delay, Runnable callback) {
        // Wrap components in AlphaContainers
        AnimationManager.AlphaContainer fromWrapper = new AnimationManager.AlphaContainer(fromComponent);
        AnimationManager.AlphaContainer toWrapper = new AnimationManager.AlphaContainer(toComponent);

        // Get parent
        Container parent = fromComponent.getParent();
        if (parent == null) return;

        // Remove original components
        parent.remove(fromComponent);
        parent.remove(toComponent);

        // Add both alpha containers
        parent.add(fromWrapper);
        parent.add(toWrapper);

        // Set bounds
        fromWrapper.setBounds(fromComponent.getBounds());
        toWrapper.setBounds(toComponent.getBounds());

        fromWrapper.setAlpha(1f);
        toWrapper.setAlpha(0f);

        toWrapper.setVisible(true);
        fromWrapper.setVisible(true);
        parent.revalidate();
        parent.repaint();

        int steps = Math.max(duration / 16, 1);
        int interval = duration / steps;

        final float[] progress = {0f};
        float delta = 1.0f / steps;

        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(new Runnable() {
            int step = 0;

            @Override
            public void run() {
                progress[0] += delta;
                if (progress[0] > 1f) progress[0] = 1f;

                SwingUtilities.invokeLater(() -> {
                    fromWrapper.setAlpha(1f - progress[0]);
                    toWrapper.setAlpha(progress[0]);
                    parent.repaint();
                });

                step++;
                if (step >= steps) {
                    SwingUtilities.invokeLater(() -> {
                        parent.remove(fromWrapper);
                        parent.remove(toWrapper);
                        parent.add(toComponent);
                        toComponent.setVisible(true);
                        parent.revalidate();
                        parent.repaint();
                        if (callback != null) callback.run();
                    });
                    throw new RuntimeException("STOP");
                }
            }
        }, delay, interval, TimeUnit.MILLISECONDS);

        scheduler.schedule(() -> task.cancel(true), duration + delay + 100, TimeUnit.MILLISECONDS);
    }

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
