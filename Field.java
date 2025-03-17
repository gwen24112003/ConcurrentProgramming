import java.util.concurrent.locks.ReentrantLock;

public class Field {
    private final AnimalType type;
    private final int FIELD_CAPACITY = 10;
    private int animalCount = 5; // Start with 5 animals
    private final ReentrantLock lock = new ReentrantLock(true); // Fair lock to prevent starvation

    public Field(AnimalType type) {
        this.type = type;
    }

    /**
     * Adds an animal to the field if there is space.
     *
     * @return True if the animal was added, false if full.
     */
    public boolean addAnimal() {
        lock.lock();
        try {
            if (animalCount < FIELD_CAPACITY) {
                animalCount++;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes an animal from the field if available.
     *
     * @return True if an animal was removed, false if empty.
     */
    public boolean removeAnimal() {
        lock.lock();
        try {
            if (animalCount > 0) {
                animalCount--;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Allows a farmer to stock multiple animals at once.
     *
     * @param animalsToAdd Number of animals the farmer wants to add.
     * @return Number of animals successfully added.
     */
    public int stockAnimals(int animalsToAdd) {
        lock.lock();
        try {
            int added = 0;
            while (animalCount < FIELD_CAPACITY && added < animalsToAdd) {
                animalCount++;
                added++;
            }
            return added;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the current count of animals in the field.
     *
     * @return Number of animals in the field.
     */
    public int getAnimalCount() {
        return animalCount;
    }

    public AnimalType getType() {
        return type;
    }

    public ReentrantLock getLock() {
        return lock;
    }
}
