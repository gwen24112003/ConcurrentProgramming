import java.util.*;

public class Farmer extends Thread {
    private final Enclosure enclosure;
    private final Map<AnimalType, Field> fields;
    private final Random rand = new Random();

    public Farmer(String name, Enclosure enclosure, Map<AnimalType, Field> fields) {
        super(name);
        this.enclosure = enclosure;
        this.fields = fields;
    }

    @Override
    public void run() {
        while (true) {
            try {
                List<AnimalType> collectedAnimals = new ArrayList<>();

                // Prendre jusqu'à 10 animaux
                while (collectedAnimals.size() < 10 && !enclosure.isEmpty()) {
                    collectedAnimals.add(enclosure.takeAnimal());
                }

                if (collectedAnimals.isEmpty()) {
                    Thread.sleep(500); // Attendre un peu si l'enclos est vide
                    continue;
                }

                System.out.println("🚜 [" + getName() + "] Collected: " + collectedAnimals);

                // Déplacer les animaux vers leurs champs respectifs
                for (AnimalType animal : collectedAnimals) {
                    Field field = fields.get(animal);

                    // Attente pour arriver au champ
                    Thread.sleep(10 * Farm.TICK_TIME);

                    field.getLock().lock();
                    try {
                        if (field.addAnimal()) {
                            System.out.println("✅ [" + getName() + "] Stocked: " + animal + " in " + field.getType());
                        } else {
                            System.out.println("❌ [" + getName() + "] Field full, could not stock: " + animal);
                        }
                    } finally {
                        field.getLock().unlock();
                    }
                }

                // Retour à l'enclos
                Thread.sleep(10 * Farm.TICK_TIME);

            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
