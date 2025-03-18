import java.util.Random;

public class FarmSimulator {
    public static void main(String[] args) {
        Farm farm = new Farm();
        TickManager tickManager = new TickManager();
        tickManager.startTicking();

        for (int i = 0; i < 3; i++) {
            new Farmer(i + 1, farm, tickManager).start();
        }

        for (int i = 0; i < 5; i++) {
            new Buyer("Buyer-" + (i + 1), farm.getFields(), tickManager).start();
        }

        new Thread(() -> {
            Random rand = new Random();
            while (true) {
                try {
                    Thread.sleep(10000);
                    AnimalType[] delivery = new AnimalType[15];
                    for (int i = 0; i < 15; i++) {
                        delivery[i] = AnimalType.values()[rand.nextInt(AnimalType.values().length)];
                    }
                    farm.getEnclosure().addAnimals(delivery);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }
}