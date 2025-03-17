public class TestField {
    public static void main(String[] args) {
        // Create a field for pigs
        Field pigField = new Field(AnimalType.PIG);

        // Display initial animal count
        System.out.println("Pig field - Initial animal count: " + pigField.getAnimalCount());

        // Attempt to add an animal
        boolean added = pigField.addAnimal();
        System.out.println("Adding a pig: " + (added ? "Success" : "Failed"));
        System.out.println("Animal count after addition: " + pigField.getAnimalCount());

        // Attempt to remove an animal
        boolean removed = pigField.removeAnimal();
        System.out.println("Removing a pig: " + (removed ? "Success" : "Failed"));
        System.out.println("Animal count after removal: " + pigField.getAnimalCount());

        // Test field capacity limit
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
