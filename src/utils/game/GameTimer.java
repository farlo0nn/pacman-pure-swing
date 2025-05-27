package utils.game;

public class GameTimer implements Runnable {
    private volatile boolean running = true;

    private final int fps;
    private final Runnable callback;

    public GameTimer(int fps, Runnable callback) {
        this.fps = fps;
        this.callback = callback;
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
        callback.run();
        long endExec = System.currentTimeMillis();
        long elapsed = endExec - startExec;
        long sleepTime = frameDuration - elapsed;
//        System.out.println(sleepTime);
        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (IllegalArgumentException e) {
                // TODO logging
            } catch (InterruptedException e) {
                // TODO logging
            }
        }
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}

