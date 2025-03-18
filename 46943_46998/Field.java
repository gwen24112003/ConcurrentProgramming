import java.util.concurrent.locks.ReentrantLock;

class Field {
    private final AnimalType type;
    private final int capacity = 10;
    private int count = 5;
    private final ReentrantLock lock = new ReentrantLock();

    public Field(AnimalType type) {
        this.type = type;
    }

    public boolean addAnimal() {
        lock.lock();
        try {
            if (count < capacity) {
                count++;
                System.out.println("Field [" + type + "] now has " + count + " animals.");
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
            if (count > 0) {
                count--;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public int getAnimalCount() {
        return count;
    }

    public ReentrantLock getLock() {
        return lock;
    }
}
