public class TestEnclosure {
    public static void main(String[] args) throws InterruptedException {
        Enclosure enclosure = new Enclosure();

        // Simulating a delivery
        AnimalType[] delivery = {AnimalType.PIG, AnimalType.COW, AnimalType.SHEEP};
        enclosure.addAnimals(delivery);

        // Check the number of animals
        System.out.println("Enclosure animal count: " + enclosure.getAnimalCount()); // (Should be 3)

        // A farmer takes an animal
        AnimalType takenAnimal = enclosure.takeAnimal();
        System.out.println("Farmer took: " + takenAnimal);

        // Check remaining animals
        System.out.println(" Remaining in enclosure: " + enclosure.getAnimalCount());
    }
}
