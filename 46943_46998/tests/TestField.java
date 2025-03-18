public class TestField {
    public static void main(String[] args) {
        // Create a field for pigs
        Field pigField = new Field(AnimalType.PIG);

        // Initial count
        System.out.println("Pig field - Initial count: " + pigField.getAnimalCount());

        // Adding an animal
        boolean added = pigField.addAnimal();
        System.out.println("Adding a pig: " + (added ? "Success" : "Failed"));
        System.out.println("New count: " + pigField.getAnimalCount());

        // Removing an animal
        boolean removed = pigField.removeAnimal();
        System.out.println("Removing a pig: " + (removed ? "Success" : "Failed"));
        System.out.println("New count: " + pigField.getAnimalCount());

        // Test field capacity
        for (int i = 0; i < 10; i++) {
            pigField.addAnimal();
        }
        System.out.println("Attempt to add when full: " + pigField.addAnimal());

        // Test empty field state
        for (int i = 0; i < 15; i++) {
            pigField.removeAnimal();
        }
        System.out.println("Attempt to remove when empty: " + pigField.removeAnimal());
    }
}
