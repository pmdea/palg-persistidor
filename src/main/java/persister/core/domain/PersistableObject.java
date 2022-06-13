package persister.core.domain;

import java.util.ArrayList;

public class PersistableObject {
    private final int id;
    private final int clazzId;
    private final int sessionId;
    private final ArrayList<ObjectField> fields;

    public PersistableObject(int id, int clazzId, int sessionId, ArrayList<ObjectField> fields) {
        this.id = id;
        this.clazzId = clazzId;
        this.sessionId = sessionId;
        this.fields = fields;
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

    public ArrayList<ObjectField> getFields() {
        return fields;
    }
}
