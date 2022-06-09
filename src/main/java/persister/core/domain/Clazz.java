package persister.core.domain;

import java.util.List;

public class Clazz {
    private final int id;

    private final String name;

    private final List<Field> fields;

    public Clazz(int id, String name, List<Field> fields) {
        this.id = id;
        this.name = name;
        this.fields = fields;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public List<Field> getFields() {
        return fields;
    }
}
