import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

class Enclosure {
    private final Queue<AnimalType> animals = new LinkedList<>();

    public synchronized void addAnimals(AnimalType[] newAnimals, long tickCount) {
        animals.addAll(Arrays.asList(newAnimals));

        Map<AnimalType, Integer> countMap = new HashMap<>();
        for (AnimalType animal : newAnimals) {
            countMap.put(animal, countMap.getOrDefault(animal, 0) + 1);
        }

        StringBuilder message = new StringBuilder();
        message.append(tickCount).append(" ")
               .append(Thread.currentThread().getName())
               .append(" Deposit_of_animals : ");

        for (Map.Entry<AnimalType, Integer> entry : countMap.entrySet()) {
            message.append(entry.getKey().toString().toLowerCase()).append("=")
                   .append(entry.getValue()).append(" ");
        }

        System.out.println(message.toString().trim());
    }    

    public synchronized AnimalType takeAnimal() {
        return animals.poll();
    }

    public synchronized int getAnimalCount() {
        return animals.size();
    }
}
