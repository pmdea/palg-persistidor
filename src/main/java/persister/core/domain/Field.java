package persister.core.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="FIELD")
public class Field {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "typeId", referencedColumnName = "id")
    private FieldType typeId;
    
    @ManyToOne
    @JoinColumn(name = "clazzId", referencedColumnName = "id")
    private Clazz clazzId;
    
    public Field() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FieldType getTypeId() {
		return typeId;
	}

	public void setTypeId(FieldType typeId) {
		this.typeId = typeId;
	}

	public Clazz getClazzId() {
		return clazzId;
	}

	public void setClazzId(Clazz clazzId) {
		this.clazzId = clazzId;
	}

	public Field(int id, String name, FieldType typeId, Clazz clazzId) {
		super();
		this.id = id;
		this.name = name;
		this.typeId = typeId;
		this.clazzId = clazzId;
	}
}
