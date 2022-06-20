package persister.core.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="FIELDTYPE")
public class FieldType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    public FieldType(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public FieldType() {}

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
