package persistor.core.domain.object;

public class Object {
    private final int id;

    private final int clazzId;
    private final int sessionId;

    public Object(int id, int clazzId, int sessionId) {
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
