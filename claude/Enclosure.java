import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class Enclosure {
    private final Queue<AnimalType> animals = new LinkedList<>();
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Adds animals to the enclosure
     * @param newAnimals Array of animals to add
     * @param tickCount Current tick count
     */
    public void addAnimals(AnimalType[] newAnimals, long tickCount) {
        lock.lock();
        try {
            for (AnimalType animal : newAnimals) {
                animals.add(animal);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Takes an animal from the enclosure
     * @return The animal taken, or null if no animals are available
     */
    public AnimalType takeAnimal() {
        lock.lock();
        try {
            return animals.poll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the number of animals in the enclosure
     * @return The number of animals
     */
    public int getAnimalCount() {
        lock.lock();
        try {
            return animals.size();
        } finally {
            lock.unlock();
        }
    }
}
