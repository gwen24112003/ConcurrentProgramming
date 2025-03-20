import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class TickManager {
    private final AtomicLong tickCount = new AtomicLong(0);
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition tickCondition = lock.newCondition();
    private final ConcurrentHashMap<Thread, Long> waitingThreads = new ConcurrentHashMap<>();
    private final long tickInterval = 100; // milliseconds per tick

    /**
     * Increments the tick count and signals all waiting threads
     */
    private void tick() {
        lock.lock();
        try {
            tickCount.incrementAndGet();
            tickCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return the current tick count
     */
    public long getTickCount() {
        return tickCount.get();
    }

    /**
     * Waits until the specified target tick is reached
     * 
     * @param targetTick The tick to wait for
     * @return The number of ticks waited
     * @throws InterruptedException if the thread is interrupted
     */
    public long waitUntilTick(long targetTick) throws InterruptedException {
        if (targetTick <= tickCount.get()) {
            return 0; // Already passed this tick
        }

        long startWaitTick = tickCount.get();
        Thread currentThread = Thread.currentThread();
        waitingThreads.put(currentThread, startWaitTick);

        lock.lock();
        try {
            while (tickCount.get() < targetTick) {
                tickCondition.await();
            }
        } finally {
            lock.unlock();
            waitingThreads.remove(currentThread);
        }

        return tickCount.get() - startWaitTick;
    }

    /**
     * Waits for the specified number of ticks to pass
     * 
     * @param ticksToWait Number of ticks to wait
     * @return The number of ticks actually waited
     * @throws InterruptedException if the thread is interrupted
     */
    public long waitForTicks(long ticksToWait) throws InterruptedException {
        return waitUntilTick(tickCount.get() + ticksToWait);
    }

    /**
     * Starts the tick thread
     */
    public void startTicking() {
        Thread tickThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(tickInterval);
                    tick();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        tickThread.setDaemon(true);
        tickThread.setName("TickThread");
        tickThread.start();
    }

    /**
     * Returns how many ticks a thread has been waiting
     * @param thread The thread to check
     * @return The number of ticks the thread has been waiting, or -1 if not waiting
     */
    public long getWaitingTicks(Thread thread) {
        Long startTick = waitingThreads.get(thread);
        if (startTick == null) {
            return -1;
        }
        return tickCount.get() - startTick;
    }
}
