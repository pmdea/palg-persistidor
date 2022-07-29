package persister;

import org.springframework.stereotype.Service;
import persister.annotations.NotPersistable;
import persister.annotations.Persistable;
import persister.core.domain.Clazz;
import persister.core.domain.Field;
import persister.core.domain.FieldType;
import persister.core.repository.FieldTypeRepository;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClazzParser {
    private FieldTypeRepository fieldTypeRepo;

    private static ClazzParser instance;

    public ClazzParser(FieldTypeRepository fieldTypeRepo) {
        this.fieldTypeRepo = fieldTypeRepo;
    }

    public static ClazzParser getInstance(FieldTypeRepository fieldTypeRepo) {
        if (instance == null) {
            instance = new ClazzParser(fieldTypeRepo);
        }
        return instance;
    }

    public static ClazzParser getInstance() {
        if (instance == null) throw new RuntimeException("ClazzParser must be initialized with fieldTypeRepo");
        return instance;
    }

    public Clazz toClazzFromObject(Object object) {
        Class clazz = object.getClass();
        return toClazz(clazz);
    }

    public Clazz toClazz(Class clazz) {
        Annotation persistable = clazz.getAnnotation(Persistable.class);
        java.lang.reflect.Field[] declaredFields = clazz.getDeclaredFields();

        List<Field> fields = Arrays.stream(declaredFields).map(field -> {
            Annotation persistableField = field.getAnnotation(Persistable.class);
            Annotation notPersistableField = field.getAnnotation(NotPersistable.class);
            boolean persistField = (persistable != null || persistableField != null) && notPersistableField == null;
            if (!persistField) return null;
            FieldType type = getFieldType(field.getType().getName());
            Field f = new Field();
            f.setName(field.getName());
            f.setType(type);
            return f;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        Clazz c = new Clazz();
        c.setName(getClassName(clazz));
        c.setFieldsParents(fields);
        return c;
    }

    public String getName(Object object) {
        Class clazz = object.getClass();
        return getClassName(clazz);
    }

    private String getClassName(Class clazz) {
        return clazz.getName();
    }

    private FieldType getFieldType(String name) {
        Optional<FieldType> type = fieldTypeRepo.findByName(name);
        if (type.isPresent()) {
            return type.get();
        }
        FieldType newFieldType = new FieldType();
        newFieldType.setName(name);
        return fieldTypeRepo.save(newFieldType);
    }
}
