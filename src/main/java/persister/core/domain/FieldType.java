package persister.core.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FieldType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private final int id;
    private final String name;

    public FieldType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
