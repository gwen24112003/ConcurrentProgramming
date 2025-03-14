import java.util.concurrent.locks.ReentrantLock;

public class Field {
    private final AnimalType type;
    private final int field_capacity = 10;
    private int animal_count = 5; 
    private final ReentrantLock lock = new ReentrantLock();

    public Field(AnimalType type) {
        this.type = type;
    }

    public boolean addAnimal() {
        lock.lock();
        try {
            if (animal_count < field_capacity) {
                animal_count++;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean removeAnimal() {
        lock.lock();
        try {
            if (animal_count > 0) {
                animal_count--;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public int getAnimalCount() {
        return animal_count;
    }

    public AnimalType getType() {
        return type;
    }

    public ReentrantLock getLock() {
        return lock;
    }
}
