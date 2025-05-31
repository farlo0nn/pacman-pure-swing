package controller.utils.timing;

public class GameTimer {

    private Thread thread;
    private long intervalMillis;
    private long currentTick;
    private Runnable task;
    private volatile boolean running = false;

    public GameTimer(long intervalMillis, Runnable task) {
        this.intervalMillis = intervalMillis;
        this.task = task;
    }

    private void tick(long intervalMillis) {
        if (Thread.currentThread().isInterrupted()) return;
        long startExec = System.currentTimeMillis();
        task.run();
        long endExec = System.currentTimeMillis();
        long elapsed = endExec - startExec;
        long sleepTime = intervalMillis - elapsed;

        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (IllegalArgumentException e) {
                // TODO logging
            } catch (InterruptedException e ) {
                Thread.currentThread().interrupt();
            }
        } else {
            Thread.yield();
        }
        currentTick++;
    }

    public void start() {
        if (running) return;
        running = true;
        thread = new Thread(() -> {
            while (running) {
                tick(intervalMillis);
            }
        });
        thread.start();
    }

    public void stop() {
        running = false;
        thread.interrupt();
    }
}
