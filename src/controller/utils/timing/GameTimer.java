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
        long startExec = System.currentTimeMillis();
        task.run();
        long endExec = System.currentTimeMillis();
        long elapsed = endExec - startExec;
        long sleepTime = intervalMillis - elapsed;

        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (IllegalArgumentException | InterruptedException e) {
                // TODO logging
            }
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
