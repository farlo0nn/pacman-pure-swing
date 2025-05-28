package utils.timing;

import java.util.ArrayList;

public class GameTimer implements Runnable {
    private volatile boolean running = true;

    private final int fps;
    private int currentTick;
    private ArrayList<RecurrentCallback> callbacks;

    public GameTimer(int fps) {
        this.fps = fps;
        callbacks = new ArrayList<>();
        currentTick = 1;
    }

    @Override
    public void run() {
        long frameDuration = 1000 / fps;
        while (running) {
            tick(frameDuration);
        }
    }

    private void tick(long frameDuration) {
        long startExec = System.currentTimeMillis();
        for (RecurrentCallback callback : callbacks) {
            if (currentTick%callback.frequency == 0){
                callback.run();
            }
        }
        long endExec = System.currentTimeMillis();
        long elapsed = endExec - startExec;
        long sleepTime = frameDuration - elapsed;

        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (IllegalArgumentException | InterruptedException e) {
                // TODO logging
            }
        }
        currentTick++;
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void addCallback(Runnable callback, int frequency) {
        callbacks.add(new RecurrentCallback(callback, frequency));
    }
    public void addCallback(Runnable callback) {
        addCallback(callback, 1);
    }
}

