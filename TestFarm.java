public class TestFarm {
    public static void main(String[] args) {
        Farm farm = new Farm();

        System.out.println("Testing fields...");
        for (AnimalType type : AnimalType.values()) {
            Field field = farm.getField(type);
            System.out.println("✔ Field: " + type + " - Initial animal count: " + field.getAnimalCount());
        }

        System.out.println("\n🔍 Testing enclosure...");
        farm.getEnclosure().addAnimals(new AnimalType[]{AnimalType.PIG, AnimalType.COW});
        System.out.println("Enclosure now contains " + farm.getEnclosure().getAnimalCount() + " animals.");
    }
}
