package persister;

import persister.core.domain.Clazz;
import persister.core.domain.PersistableObject;
import persister.core.domain.Session;

import java.util.ArrayList;

public class ObjectParser {
    public PersistableObject toPersistable(Object object, Clazz clazz, Session session) {
        //todo: armar fields en base a los fields de clazz
        return new PersistableObject(0,clazz.getId(), session.getId(), new ArrayList<>());
    }

    public Object toObject(PersistableObject persistableObject) {
        return 1; //TODO
    }
}
