package persister;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import persister.core.domain.*;
import persister.core.repository.ClazzRepository;
import persister.core.repository.FieldRepository;
import persister.core.repository.ObjectFieldRepository;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ObjectParser {
	
	private FieldRepository fieldRepo;
	
    private ObjectFieldRepository objectFieldRepo;

    private static ObjectParser instance;

    public ObjectParser(ObjectFieldRepository objectFieldRepo, FieldRepository fieldRepo) {
        this.objectFieldRepo = objectFieldRepo;
        this.fieldRepo = fieldRepo;
    }

    public PersistableObject toPersistable(PersistableObject persistableObject, Object object, Clazz c, Session session) {
        persistableObject.setClazzId(c);
        persistableObject.setSessionId(session);

        //Mostrar lo necesario de una clase
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        c.getFields().forEach(cf -> System.out.println("CLASS FIELD NAME " + cf.getName()));
        //Creo el HEADER
        ObjectField header = new ObjectField();
        header.setFieldId(null);
        header.setValue(null);
        header.setObjectId(persistableObject);
        header.setParentId(null);
        header.setNombre("HEADER");
        header = objectFieldRepo.save(header);
        persistableObject.setObjectFields(new ArrayList<>());
        persistableObject.getObjectFields().add(0, header);
        for(Field field : fields) {

            System.out.println("FIELD NAME " + field.getName());
            Optional<persister.core.domain.Field> classField = c.getFields().stream().filter(cf -> cf.getName().equals(field.getName())).findFirst();
            if (classField.isPresent()) {
                System.out.println("PRESENTE");
                saveAndGetObjectField(object, field, classField.get(), persistableObject, header.getId());
            } else {
                System.out.println("NO PRESENTE");
            }
        }

        return persistableObject;
    }

    private void saveAndGetObjectField(Object object, Field field, persister.core.domain.Field classField, PersistableObject pObject, Integer parentId) {
        ObjectField objectField = new ObjectField();
        objectField.setFieldId(classField);
    	objectField.setParentId(parentId);
    	objectField.setObjectId(pObject);
        try {
            field.setAccessible(true);
            Object fieldValue = field.get(object);
            String fieldType = field.getType().getName();
            if (Types.getInstance().isPrimitive(fieldType)) {
                ObjectMapper mapper = new ObjectMapper();
                persister.core.domain.Field fieldTabla = new persister.core.domain.Field();
                fieldTabla.setName(field.getName());
                fieldTabla = fieldRepo.save(fieldTabla);
                objectField.setFieldId(fieldTabla);
                objectField.setValue(mapper.writeValueAsString(fieldValue));
                objectField = objectFieldRepo.save(objectField);
                objectField.setNombre(fieldType);
                pObject.getObjectFields().add(objectField);
            } 
            else if(Types.getInstance().isObject(field)) {
                objectField.setNombre(fieldType);
                objectField = objectFieldRepo.save(objectField);
                persister.core.domain.Field fieldTabla = new persister.core.domain.Field();
                fieldTabla.setName(field.getName());
                fieldTabla = fieldRepo.save(fieldTabla);
                objectField.setFieldId(fieldTabla);
                pObject.getObjectFields().add(objectField);
                Class objClass = fieldValue.getClass();
                for(Field f: objClass.getDeclaredFields()) {
                    saveAndGetObjectField(fieldValue, f, fieldTabla, pObject, objectField.getId());
                }
            }
            else if(Types.getInstance().isListPrimitive(field)) {
                objectField.setNombre(fieldType);
                objectField = objectFieldRepo.save(objectField);
                pObject.getObjectFields().add(objectField);
                List<Object> list = (List<Object>) fieldValue;
                for (int i = 0; i< list.size() ; i ++) {
                    Object e = list.get(i);
                    ObjectField eObjectField = getNestedObjectField(e);
                    eObjectField.setObjectId(pObject);
                    eObjectField.setParentId(objectField.getId());
                    objectFieldRepo.save(eObjectField);
                    pObject.getObjectFields().add(eObjectField);
                }
            }
            else if(Types.getInstance().isListObject(field)) {
                objectField.setNombre(fieldType);
                objectField = objectFieldRepo.save(objectField);
                pObject.getObjectFields().add(objectField);
                List<Object> list = (List<Object>) fieldValue;
                for (int i = 0; i< list.size() ; i ++) {
            		ParameterizedType listType = (ParameterizedType)field.getGenericType();
                    Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
                    ObjectField objectListField = new ObjectField();
                    persister.core.domain.Field fieldTabla = new persister.core.domain.Field();
                    fieldTabla.setName(field.getName());
                    fieldTabla = fieldRepo.save(fieldTabla);
                    objectListField.setFieldId(fieldTabla);
                    objectListField.setParentId(objectField.getId());
                    objectListField.setObjectId(pObject);
                    objectListField.setNombre(listClass.getName());
                    objectFieldRepo.save(objectListField);
                    pObject.getObjectFields().add(objectListField);
                	for(Field f : listClass.getDeclaredFields()) {
                        saveAndGetObjectField(list.get(i), f, fieldTabla, pObject, objectListField.getId());
                	}
                }
            }
        } catch (IllegalAccessException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private ObjectField getNestedObjectField(Object e) {
        ObjectField of = new ObjectField();
        Object fieldValue = e;
        String fieldType = e.getClass().getName();
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
