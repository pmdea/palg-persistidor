package persister;

import persister.core.domain.PersistableObject;

public class ObjectParser {
    public PersistableObject toPersistable(Object object) {
        return new PersistableObject();
    }

    public Object toObject(PersistableObject persistableObject) {
        return 1; //TODO
    }
}
