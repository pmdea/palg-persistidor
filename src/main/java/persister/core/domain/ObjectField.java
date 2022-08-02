package persister.core.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	
	@Column(name = "parentId")
    private Integer parentId;
	
	@Column(name = "valor")
    private String valor;
	
	@Column(name = "nombre")
	private String nombre;
    
    public ObjectField() {}

	public Integer getId() {
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getValue() {
		return valor;
	}

	public void setValue(String valor) {
		this.valor = valor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
    
    
}
