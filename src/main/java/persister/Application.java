package persister;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import persister.annotations.NotPersistable;
import persister.annotations.Persistable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        PersonaTest p = new PersonaTest();
        p.setDni(4040);
        p.setNombre("Jorge");

        //Mostrar lo necesario de una clase
        Class clazz = p.getClass();
        Annotation persistable = clazz.getAnnotation(Persistable.class);
        Field[] fields = clazz.getDeclaredFields();

        System.out.println("Class: " + clazz.getSimpleName() + "(" + clazz.getName() + ")");

        System.out.println("Persistable: "+ (persistable != null));

        System.out.println("Fields:");
        Arrays.stream(fields).forEach(field -> {
            System.out.println("\t"+field.getName());
            System.out.println("\t\tType: "+field.getType().getName());
            Annotation persistableField = field.getAnnotation(Persistable.class);
            Annotation notPersistableField = field.getAnnotation(NotPersistable.class);
            Boolean persistField = (persistable != null || persistableField != null) && notPersistableField == null;
            System.out.println("\t\tPersistable: "+ persistField);
            try {
                ObjectMapper mapper = new ObjectMapper();
                String v = mapper.writeValueAsString(field.get(p));
                List list = mapper.readValue(v, List.class);
                field.setAccessible(true);
                System.out.println("\t\tValor: " + field.get(v));
            } catch (IllegalAccessException | IOException e) {
                e.printStackTrace();
            }
        });

        // Reconstruir objeto
        try {
            Constructor[] constructorWithoutParameters = clazz.getConstructors();

            PersonaTest newP = (PersonaTest) constructorWithoutParameters[0].newInstance();
            Arrays.stream(fields).forEach(field -> {
                Annotation persistableField = field.getAnnotation(Persistable.class);
                Annotation notPersistableField = field.getAnnotation(NotPersistable.class);
                Boolean persistField = (persistable != null || persistableField != null) && notPersistableField == null;
                if (!persistField) return;
                try {
                    if (field.getType().getName() == "int")
                        field.set(newP, 4040);
                    else
                        field.set(newP, "Pepe");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });

            System.out.println("\nInfo del nuevo objeto:");
            System.out.println("dni: "+newP.getDni());
            System.out.println("nombre: "+newP.getNombre());

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
            e.printStackTrace();
        }
    }
}
