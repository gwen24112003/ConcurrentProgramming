import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;

class Enclosure {
    private final Queue<AnimalType> animals = new LinkedList<>();

    public synchronized void addAnimals(AnimalType[] newAnimals) {
        animals.addAll(Arrays.asList(newAnimals));
        System.out.println("🚚 [Delivery] Added " + newAnimals.length + " animals to enclosure.");
    }    

    public synchronized AnimalType takeAnimal() {
        return animals.poll();
    }

    public synchronized int getAnimalCount() {
        return animals.size();
    }
}
