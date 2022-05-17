package persister.core.domain;

public class Field {
    private final int id;
    private final String name;
    private final int typeId;
    private final int clazzId;

    public Field(int id, String name, int typeId, int clazzId) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.clazzId = clazzId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getClazzId() {
        return clazzId;
    }
}
