package persister.core.domain.objectField;

public class ObjectField {
    private final int id;
    private final int objectId;
    private final int fieldId;
    private final int nestedObjectFieldId;
    private final int valueObjectId;
    private final String value;

    public ObjectField(int id, int objectId, int fieldId, int nestedObjectFieldId, int valueObjectId, String value) {
        this.id = id;
        this.objectId = objectId;
        this.fieldId = fieldId;
        this.nestedObjectFieldId = nestedObjectFieldId;
        this.valueObjectId = valueObjectId;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getFieldId() {
        return fieldId;
    }

    public int getNestedObjectFieldId() {
        return nestedObjectFieldId;
    }

    public int getValueObjectId() {
        return valueObjectId;
    }

    public String getValue() {
        return value;
    }
}