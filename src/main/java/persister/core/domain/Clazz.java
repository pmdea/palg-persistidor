package persister.core.domain;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="CLAZZ")
public class Clazz {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @OneToMany(mappedBy = "clazzId", cascade = CascadeType.ALL)
    private List<Field> fields;
    @OneToMany(mappedBy = "clazzId", cascade = CascadeType.ALL)
    private List<PersistableObject> persistableObjects;

    public Clazz(int id, String name, List<Field> fields) {
        this.id = id;
        this.name = name;
        this.fields = fields;
    }
    
    public Clazz() {}
    
	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public void setFieldsParents(List<Field> fields) {
		for(Field f: fields) {
			f.setClazz(this);
		}
		this.fields = fields;
	}

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
}
