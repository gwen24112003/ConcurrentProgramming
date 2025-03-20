import java.util.concurrent.locks.ReentrantLock;

public class Field {
    private final AnimalType type;
    private final int capacity = 10;
    private int count = 5; // Initial count of animals
    private final ReentrantLock lock = new ReentrantLock();
    private boolean beingStocked = false;

    public Field(AnimalType type) {
        this.type = type;
    }

    /**
     * Adds an animal to the field if there is space
     * @return true if the animal was added, false otherwise
     */
    public boolean addAnimal() {
        // Lock is handled by the caller
        if (count < capacity) {
            count++;
            return true;
        }
        return false;
    }

    /**
     * Removes an animal from the field if there is one
     * @return true if an animal was removed, false otherwise
     */
    public boolean removeAnimal() {
        // Lock is handled by the caller
        if (count > 0) {
            count--;
            return true;
        }
        return false;
    }

    public int getAnimalCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public boolean isBeingStocked() {
        return beingStocked;
    }

    public void setBeingStocked(boolean beingStocked) {
        this.beingStocked = beingStocked;
    }
    
    public AnimalType getType() {
        return type;
    }
}
