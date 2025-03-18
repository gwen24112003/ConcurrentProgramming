import java.util.HashMap;
import java.util.Map;

class Farm {
    private final Map<AnimalType, Field> fields = new HashMap<>();
    private final Enclosure enclosure = new Enclosure();

    public Farm() {
        for (AnimalType type : AnimalType.values()) {
            fields.put(type, new Field(type));
        }
    }

    public Field getField(AnimalType type) { return fields.get(type); }
    public Enclosure getEnclosure() { return enclosure; }
    public Map<AnimalType, Field> getFields() { return fields; }
}
