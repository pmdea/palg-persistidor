package persister;

import persister.annotations.NotPersistable;
import persister.annotations.Persistable;
import persister.core.domain.Clazz;
import persister.core.domain.Field;
import persister.core.domain.FieldType;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClazzParser {
    public <T> Clazz toClazz(Object object) {
        Class clazz = object.getClass();
        String name = getClassName(clazz);

        Annotation persistable = clazz.getAnnotation(Persistable.class);
        java.lang.reflect.Field[] declaredFields = clazz.getDeclaredFields();

//        List<Field> fields = Arrays.stream(declaredFields).map(field -> {
//            Annotation persistableField = field.getAnnotation(Persistable.class);
//            Annotation notPersistableField = field.getAnnotation(NotPersistable.class);
//            boolean persistField = (persistable != null || persistableField != null) && notPersistableField == null;
//            if (!persistField) return null;
//            return new Field(0, field.getName(), new FieldType(0, field.getType().getName())); //Lo dejo mal porque cambiaron los entities
//        }).filter(Objects::nonNull).collect(Collectors.toList());

        return new Clazz(0, name, null);
    }

    public String getName(Object object) {
        Class clazz = object.getClass();
        return getClassName(clazz);
    }

    private String getClassName(Class clazz) {
        return clazz.getName();
    }
}
