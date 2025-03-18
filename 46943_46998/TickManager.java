class TickManager {
    private long tickCount = 0;
    private final long tickInterval = 100;
    private final Object lock = new Object();

    public void tick() {
        synchronized (lock) {
            tickCount++;
            lock.notifyAll();
        }
    }

    public long getTickCount() {
        synchronized (lock) {
            return tickCount;
        }
    }

    public void waitForNextTick(long currentTick) throws InterruptedException {
        synchronized (lock) {
            while (tickCount <= currentTick) {
                lock.wait();
            }
        }
    }

    public void startTicking() {
        Thread tickThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(tickInterval);
                    tick();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        tickThread.setDaemon(true);
        tickThread.start();
    }
}
