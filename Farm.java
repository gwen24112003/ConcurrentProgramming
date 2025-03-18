import java.util.HashMap;
import java.util.Map;

/**
 * The central farm management system, handling fields and the enclosure.
 */
public class Farm {
    private final Map<AnimalType, Field> fields = new HashMap<>();
    private final Enclosure enclosure = new Enclosure();

    public Farm() {
        // Creation of a field for each animal type
        for (AnimalType type : AnimalType.values()) {
            fields.put(type, new Field(type));
        }
    }

    public Field getField(AnimalType type) {
        return fields.get(type);
    }

    public Enclosure getEnclosure() {
        return enclosure;
    }
}
