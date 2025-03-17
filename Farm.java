import java.util.*;

public class Farm {
    public static final int TICK_TIME = 100;
    private final Enclosure enclosure = new Enclosure();
    private final Map<AnimalType, Field> fields = new HashMap<>();
    private final List<Farmer> farmers = new ArrayList<>();
    private final List<Buyer> buyers = new ArrayList<>();
    private final Random rand = new Random();

    public Farm() {
        for (AnimalType type : AnimalType.values()) {
            fields.put(type, new Field(type));
        }
    }

    public void startSimulation(int farmerCount, int buyerCount) {
        for (int i = 0; i < farmerCount; i++) {
            Farmer farmer = new Farmer("Farmer-" + i, enclosure, fields);
            farmers.add(farmer);
            farmer.start();
        }

        for (int i = 0; i < buyerCount; i++) {
            Buyer buyer = new Buyer("Buyer-" + i, fields);
            buyers.add(buyer);
            buyer.start();
        }

        new Thread(this::generateDeliveries).start();
    }

    private void generateDeliveries() {
        while (true) {
            try {
                Thread.sleep(rand.nextInt(200) * TICK_TIME);
                AnimalType[] delivery = new AnimalType[10];
                for (int i = 0; i < 10; i++) {
                    delivery[i] = AnimalType.values()[rand.nextInt(AnimalType.values().length)];
                }
                enclosure.addAnimals(delivery);
            } catch (InterruptedException ignored) {}
        }
    }
}
