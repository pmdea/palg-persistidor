package persister.core.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="OBJECTFIELD")
public class ObjectField {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "objectId", referencedColumnName = "id")
    private PersistableObject objectId;
	
	@OneToOne
    @JoinColumn(name = "fieldId", referencedColumnName = "id")
    private Field fieldId;
	
	@OneToOne
    @JoinColumn(name = "nestedObjectFieldId", referencedColumnName = "id")
    private ObjectField nestedObjectFieldId;
	
	@OneToOne
    @JoinColumn(name = "valueObjectId", referencedColumnName = "id")
    private PersistableObject valueObjectId;
	
	@Column(name = "valor")
    private String valor;

	@Column(name = "nestedObjectType")
	private String nestedObjectFieldType;
    
    public ObjectField() {}
    
	public ObjectField(int id, PersistableObject objectId, Field fieldId, ObjectField nestedObjectFieldId,
			PersistableObject valueObjectId, String value) {
		super();
		this.id = id;
		this.objectId = objectId;
		this.fieldId = fieldId;
		this.nestedObjectFieldId = nestedObjectFieldId;
		this.valueObjectId = valueObjectId;
		this.valor = value;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PersistableObject getObjectId() {
		return objectId;
	}

	public void setObjectId(PersistableObject objectId) {
		this.objectId = objectId;
	}

	public Field getFieldId() {
		return fieldId;
	}

	public void setFieldId(Field fieldId) {
		this.fieldId = fieldId;
	}

	public ObjectField getNestedObjectFieldId() {
		return nestedObjectFieldId;
	}

	public void setNestedObjectFieldId(ObjectField nestedObjectFieldId) {
		this.nestedObjectFieldId = nestedObjectFieldId;
	}

	public PersistableObject getValueObjectId() {
		return valueObjectId;
	}

	public void setValueObjectId(PersistableObject valueObjectId) {
		this.valueObjectId = valueObjectId;
	}

	public String getValue() {
		return valor;
	}

	public void setValue(String value) {
		this.valor = value;
	}


	public String getNestedObjectFieldType() {
		return nestedObjectFieldType;
	}

	public void setNestedObjectFieldType(String nestedObjectFieldType) {
		this.nestedObjectFieldType = nestedObjectFieldType;
	}
}
