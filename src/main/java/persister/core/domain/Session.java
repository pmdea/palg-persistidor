package persister.core.domain;

public class Session {
    private final int id;

    private final long timestamp;

    public Session(int id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }
}