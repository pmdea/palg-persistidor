package persister;

import persister.core.domain.Clazz;
import persister.core.domain.Field;
import persister.core.repository.ClazzRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CompareClass {
    public static boolean existsDifferentInDB(ClazzRepository clazzRepo, Clazz clazz) {
        String className = clazz.getName();
        Clazz existingClazz = clazzRepo.findByName(className);
        if (existingClazz == null) return false;
        return !areEqual(clazz, existingClazz);
    }

    private static boolean areEqual(Clazz a, Clazz b) {
        List<Field> fieldsA = a.getFields();
        List<Field> fieldsB = b.getFields();

        if (fieldsA.size() != fieldsB.size()) return false;

        for (Field fieldA: fieldsA) {
            Optional<Field> fieldB = fieldsB.stream()
                    .filter(f -> Objects.equals(f.getName(), fieldA.getName()))
                    .findFirst();
            if (!fieldB.isPresent()) return false;
            if (!Objects.equals(fieldB.get().getType().getName(), fieldA.getType().getName())) return false;
        }
        return true;
    }
}
