package persister.core.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PersistableObject {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int clazzId;
    private int sessionId;

    public PersistableObject(int id, int clazzId, int sessionId) {
        this.id = id;
        this.clazzId = clazzId;
        this.sessionId = sessionId;
    }

    public int getId() {
        return id;
    }

    public int getClazzId() {
        return clazzId;
    }

    public int getSessionId() {
        return sessionId;
    }
}
