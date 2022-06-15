package persister.core.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Clazz {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    
    @OneToMany(mappedBy = "clazzId")
    private List<Field> fields;

    public Clazz(int id, String name, List<Field> fields) {
        this.id = id;
        this.name = name;
        this.fields = fields;
    }

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
    
    
}
