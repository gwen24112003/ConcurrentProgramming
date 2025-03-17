public class TickManager {
    private long tickCount = 0;
    private final long tickInterval = 100; // 100 ms par tick
    
    // Incrémente le compteur de ticks
    public synchronized void tick() {
        tickCount++;
    }

    // Retourne le nombre de ticks
    public synchronized long getTickCount() {
        return tickCount;
    }

    // Démarre un thread pour gérer le compteur de ticks
    public void startTicking() {
        Thread tickThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(tickInterval); // Attendre la durée d'un tick
                    tick(); // Incrémente le compteur
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        tickThread.setDaemon(true); // Laisser ce thread se terminer quand le programme principal se termine
        tickThread.start();
    }
}
