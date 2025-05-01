package com.btbmina.gamestore.animation;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

public class SlideEffect {
    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);

    public enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

    public static void slideIn(JComponent component, Point from, Point to, int duration, int delay, Runnable callback) {
        component.setLocation(from);
        component.setVisible(true);

        int steps = Math.max(duration / 16, 1);
        int interval = duration / steps;

        int dx = (to.x - from.x);
        int dy = (to.y - from.y);
        float stepX = dx / (float) steps;
        float stepY = dy / (float) steps;

        final float[] currentX = {from.x};
        final float[] currentY = {from.y};

        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(new Runnable() {
            int step = 0;

            @Override
            public void run() {
                currentX[0] += stepX;
                currentY[0] += stepY;

                SwingUtilities.invokeLater(() -> {
                    component.setLocation(Math.round(currentX[0]), Math.round(currentY[0]));
                    component.repaint();
                });

                step++;
                if (step >= steps) {
                    SwingUtilities.invokeLater(() -> {
                        component.setLocation(to);
                        if (callback != null) callback.run();
                    });
                    throw new RuntimeException("STOP");
                }
            }
        }, delay, interval, TimeUnit.MILLISECONDS);

        scheduler.schedule(() -> task.cancel(true), duration + delay + 100, TimeUnit.MILLISECONDS);
    }

    public static void slideOut(JComponent component, Point to, int duration, int delay, Runnable callback) {
        Point from = component.getLocation();

        int steps = Math.max(duration / 16, 1);
        int interval = duration / steps;

        int dx = (to.x - from.x);
        int dy = (to.y - from.y);
        float stepX = dx / (float) steps;
        float stepY = dy / (float) steps;

        final float[] currentX = {from.x};
        final float[] currentY = {from.y};

        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(new Runnable() {
            int step = 0;

            @Override
            public void run() {
                currentX[0] += stepX;
                currentY[0] += stepY;

                SwingUtilities.invokeLater(() -> {
                    component.setLocation(Math.round(currentX[0]), Math.round(currentY[0]));
                    component.repaint();
                });

                step++;
                if (step >= steps) {
                    SwingUtilities.invokeLater(() -> {
                        component.setLocation(to);
                        component.setVisible(false);
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
