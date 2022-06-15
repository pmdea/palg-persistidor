package persister.core.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sesion")
public class Session {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private long last_access;

    public Session(int id, long timestamp) {
        this.id = id;
        this.last_access = timestamp;
    }

	public int getId() {
		return id;
	}

	public long getLast_access() {
		return last_access;
	}
    
    
}
