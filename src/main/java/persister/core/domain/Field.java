package persister.core.domain;

public class Field {
    private final int id;
    private final String name;
    private final FieldType type;

    public Field(int id, String name, FieldType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FieldType getType() {
        return type;
    }
}
