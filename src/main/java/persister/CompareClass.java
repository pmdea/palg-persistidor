package persister;

import persister.core.domain.Clazz;
import persister.core.domain.Field;
import persister.core.repository.ClazzRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CompareClass {
    // 0 -> no existe
    // 1 -> existe
    // 2 -> existe y es distinta
    public static int existsInDB(ClazzRepository clazzRepo, Clazz clazz) {
        String className = clazz.getName();
        Clazz existingClazz = clazzRepo.findByName(className);
        if (existingClazz == null) return 0;
        if (areEqual(clazz, existingClazz)) {
            return 1;
        } else {
            return 2;
        }
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
