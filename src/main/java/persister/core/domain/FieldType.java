package persister.core.domain;

public class FieldType {
    private final int id;
    private final String name;

    public FieldType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
