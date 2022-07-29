package persister.core.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SESION")
public class Session {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long last_access;
    
    @OneToMany(mappedBy = "sessionId", cascade = CascadeType.ALL)
    private List<PersistableObject> persistableObjectList;

    public Session() {}

	public Session(int id, long last_access) {
		super();
		this.id = id;
		this.last_access = last_access;
	}
	
	public List<PersistableObject> getPersistableObject() {
		return persistableObjectList;
	}

	public void setPersistableObject(List<PersistableObject> persistableObject) {
		this.persistableObjectList = persistableObject;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLast_access() {
		return last_access;
	}

	public void setLast_access(long last_access) {
		this.last_access = last_access;
	}
    
    
}
