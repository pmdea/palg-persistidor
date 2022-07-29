package persister;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import persister.core.domain.*;
import persister.core.repository.ObjectFieldRepository;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ObjectParser {
    private ObjectFieldRepository objectFieldRepo;

    private static ObjectParser instance;

    public ObjectParser(ObjectFieldRepository objectFieldRepo) {
        this.objectFieldRepo = objectFieldRepo;
    }

    public PersistableObject toPersistable(PersistableObject persistableObject, Object object, Clazz c, Session session) {
        persistableObject.setClazzId(c);
        persistableObject.setSessionId(session);

        //Mostrar lo necesario de una clase
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        c.getFields().forEach(cf -> System.out.println("CLASS FIELD NAME " + cf.getName()));

        List<ObjectField> objectFields = Arrays.stream(fields).map(field -> {
            System.out.println("FIELD NAME " + field.getName());
            Optional<persister.core.domain.Field> classField = c.getFields().stream().filter(cf -> Objects.equals(cf.getName(), field.getName())).findFirst();

            if (classField.isPresent()) {
                System.out.println("PRESENTE");
                ObjectField objectField = saveAndGetObjectField(object, field, classField.get(), persistableObject);
                return objectField;
            } else {
                System.out.println("NO PRESENTE");
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        persistableObject.setObjectFields(objectFields);

        return persistableObject;
    }

    private ObjectField saveAndGetObjectField(Object object, Field field, persister.core.domain.Field classField, PersistableObject pObject) {
        ObjectField objectField = new ObjectField();
        objectField.setFieldId(classField);
        try {
            field.setAccessible(true);
            Object fieldValue = field.get(object);
            String fieldType = field.getType().getName();
            if (Types.getInstance().isPrimitive(fieldType)) {
                ObjectMapper mapper = new ObjectMapper();
                objectField.setValue(mapper.writeValueAsString(fieldValue));
                objectField.setObjectId(pObject);
                objectField.setFieldId(classField);
            } else if(Types.getInstance().isList(fieldType)) {
                List<Object> list = (List<Object>) fieldValue;
                ObjectField lastElement = null;
                for (int i = list.size() - 1; i >= 0; i--) {
                    Object e = list.get(i);
                    ObjectField eObjectField = getNestedObjectField(e);
                    if (lastElement != null) {
                        eObjectField.setNestedObjectFieldId(lastElement);
                    }
                    objectFieldRepo.save(eObjectField);
                    lastElement = eObjectField;
                }
                objectField.setNestedObjectFieldId(lastElement);
            }
        } catch (IllegalAccessException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return objectField;
    }

    private ObjectField getNestedObjectField(Object e) {
        ObjectField of = new ObjectField();
        Object fieldValue = e;
        String fieldType = e.getClass().getName();
        of.setNestedObjectFieldType(fieldType);
        if (Types.getInstance().isPrimitive(fieldType)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                of.setValue(mapper.writeValueAsString(fieldValue));
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        }
        return of;
    }

    public Object toObject(PersistableObject persistableObject) {
        return 1; //TODO
    }
}
