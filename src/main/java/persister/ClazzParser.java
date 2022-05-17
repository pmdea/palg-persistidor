package persister;

import persister.core.domain.Clazz;

public class ClazzParser {
    public <T> Clazz toClazz(Class<T> klazz) {
        return new Clazz(1, "klazz");
    }
}
