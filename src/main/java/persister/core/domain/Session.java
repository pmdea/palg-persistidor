package persister.core.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SESION")
public class Session {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private long last_access;
    
    @OneToOne(mappedBy = "sessionId", cascade = CascadeType.ALL)
    private PersistableObject persistableObject;

    public Session() {}

	public Session(int id, long last_access) {
		super();
		this.id = id;
		this.last_access = last_access;
	}
	
	public PersistableObject getPersistableObject() {
		return persistableObject;
	}

	public void setPersistableObject(PersistableObject persistableObject) {
		this.persistableObject = persistableObject;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getLast_access() {
		return last_access;
	}

	public void setLast_access(long last_access) {
		this.last_access = last_access;
	}
    
    
}
