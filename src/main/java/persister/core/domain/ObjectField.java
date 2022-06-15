package persister.core.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ObjectField {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int objectId;
    private int fieldId;
    private int nestedObjectFieldId;
    private int valueObjectId;
    private String value;

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
