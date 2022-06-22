package persister;

import persister.annotations.NotPersistable;
import persister.annotations.Persistable;
import persister.core.domain.Clazz;
import persister.core.domain.Field;
import persister.core.domain.FieldType;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClazzParser {
    public static Clazz toClazzFromObject(Object object) {
        Class clazz = object.getClass();
        return toClazz(clazz);
    }

    public static Clazz toClazz(Class clazz) {
        Annotation persistable = clazz.getAnnotation(Persistable.class);
        java.lang.reflect.Field[] declaredFields = clazz.getDeclaredFields();

        List<Field> fields = Arrays.stream(declaredFields).map(field -> {
            Annotation persistableField = field.getAnnotation(Persistable.class);
            Annotation notPersistableField = field.getAnnotation(NotPersistable.class);
            boolean persistField = (persistable != null || persistableField != null) && notPersistableField == null;
            if (!persistField) return null;
            FieldType type = new FieldType();
            type.setName(field.getType().getName());
            Field f = new Field(); //Lo dejo mal porque cambiaron los entities
            f.setName(field.getName());
            f.setType(type);
            return f;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        Clazz c = new Clazz();
        c.setName(getClassName(clazz));
        c.setFields(fields);
        return c;
    }

    public static String getName(Object object) {
        Class clazz = object.getClass();
        return getClassName(clazz);
    }

    private static String getClassName(Class clazz) {
        return clazz.getName();
    }
}
