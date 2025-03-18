import java.util.Map;
import java.util.Random;

class Buyer extends Thread {
    private final Map<AnimalType, Field> fields;
    private final TickManager tickManager;
    private final Random rand = new Random();

    public Buyer(String name, Map<AnimalType, Field> fields, TickManager tickManager) {
        super(name);
        this.fields = fields;
        this.tickManager = tickManager;
    }

    @Override
    public void run() {
        try {
            while (true) {
                tickManager.waitForNextTick(tickManager.getTickCount());
                AnimalType chosenType = AnimalType.values()[rand.nextInt(AnimalType.values().length)];
                Field field = fields.get(chosenType);
                field.getLock().lock();
                try {
                    if (field.removeAnimal()) {
                        System.out.println("🛒 [" + getName() + "] Bought: " + chosenType);
                    } else {
                        System.out.println("... [" + getName() + "] Waiting for: " + chosenType);
                    }
                } finally {
                    field.getLock().unlock();
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}