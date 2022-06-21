package persister.core.domain;

import java.util.List;

import javax.persistence.CascadeType;
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
@Table(name="PERSISTABLEOBJECT")
public class PersistableObject {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clazzId", referencedColumnName = "id")
    private Clazz clazzId;
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sessionId", referencedColumnName = "id")
    private Session sessionId;
	@OneToMany(mappedBy = "objectId", cascade = CascadeType.ALL)
	private List<ObjectField> objectFields;
    
	public PersistableObject() {}

	public PersistableObject(int id, Clazz clazzId, Session sessionId, List<ObjectField> objectFields) {
		super();
		this.id = id;
		this.clazzId = clazzId;
		this.sessionId = sessionId;
		this.objectFields = objectFields;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Clazz getClazzId() {
		return clazzId;
	}

	public void setClazzId(Clazz clazzId) {
		this.clazzId = clazzId;
	}

	public Session getSessionId() {
		return sessionId;
	}

	public void setSessionId(Session sessionId) {
		this.sessionId = sessionId;
	}

	public List<ObjectField> getObjectFields() {
		return objectFields;
	}

	public void setObjectFields(List<ObjectField> objectFields) {
		this.objectFields = objectFields;
	}
	public void setObjectFieldsParents(List<ObjectField> objectFields) {
		for (ObjectField objectField : objectFields) {
			objectField.setObjectId(this);
		}
		this.objectFields = objectFields;
	}
	
	
	
}
