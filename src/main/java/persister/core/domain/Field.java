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
    private FieldType type;
    
    @ManyToOne
    @JoinColumn(name = "clazzId", referencedColumnName = "id")
    private Clazz clazz;
    
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

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	public Field(int id, String name, FieldType type, Clazz clazz) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.clazz = clazz;
	}
}
