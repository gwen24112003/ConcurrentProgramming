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
            Random randgen = new Random();
            long nextDeliveryTick = tickManager.getTickCount() + (int) (2 * randgen.nextDouble() * 100);

            while (true) {
                try {
                    tickManager.waitForNextTick(nextDeliveryTick);
                    
                    AnimalType[] delivery = new AnimalType[10];
                    for (int i = 0; i < 10; i++) {
                        delivery[i] = AnimalType.values()[randgen.nextInt(AnimalType.values().length)];
                    }

                    farm.getEnclosure().addAnimals(delivery, tickManager.getTickCount());

                    // Planifier la prochaine livraison
                    nextDeliveryTick = tickManager.getTickCount() + (int) (2 * randgen.nextDouble() * 100);


                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }
}