public class TestEnclosure {
    public static void main(String[] args) throws InterruptedException {
        Enclosure enclosure = new Enclosure();

        // Simulate a delivery of animals
        AnimalType[] delivery = {AnimalType.PIG, AnimalType.COW, AnimalType.SHEEP};
        enclosure.addAnimals(delivery);

        // Display the number of animals in the enclosure
        System.out.println("Enclosure animal count: " + enclosure.getAnimalCount());

        // Farmer takes an animal
        AnimalType takenAnimal = enclosure.takeAnimal();
        System.out.println("Farmer took: " + takenAnimal);

        // Display remaining animals
        System.out.println("Remaining in enclosure: " + enclosure.getAnimalCount());
    }
}
