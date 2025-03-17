import java.util.Map;
import java.util.Random;

public class Buyer extends Thread {
    private final Map<AnimalType, Field> fields;
    private final Random rand = new Random();

    public Buyer(String name, Map<AnimalType, Field> fields) {
        super(name);
        this.fields = fields;
    }

    @Override
    public void run() {
        while (true) {
            try {
                AnimalType chosenType = AnimalType.values()[rand.nextInt(AnimalType.values().length)];
                Field field = fields.get(chosenType);

                field.getLock().lock();
                try {
                    if (field.removeAnimal()) {
                        System.out.println("🛒 [" + getName() + "] Bought: " + chosenType);
                    } else {
                        System.out.println("⚠️ [" + getName() + "] Waiting for: " + chosenType);
                    }
                } finally {
                    field.getLock().unlock();
                }

                Thread.sleep(10 * Farm.TICK_TIME);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
