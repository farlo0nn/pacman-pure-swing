package utils.timing;

public class RecurrentCallback {
    Runnable callback;
    int frequency;

    public RecurrentCallback(Runnable callback, int frequency) {
        this.callback = callback;
        this.frequency = frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void run() {
        callback.run();
    }
}
