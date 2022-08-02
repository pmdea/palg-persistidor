package persister;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Types {
    private static Types instance;
    private List<String> primitives;
    private List<String> lists;

    private Types() {
        this.primitives = new ArrayList<>();
        this.primitives.add("byte");
        this.primitives.add("short");
        this.primitives.add("int");
        this.primitives.add("long");
        this.primitives.add("float");
        this.primitives.add("double");
        this.primitives.add("char");
        this.primitives.add("boolean");
        this.primitives.add("java.lang.String");


        this.lists = new ArrayList<>();
        this.lists.add("java.util.List");
        this.lists.add("java.util.ArrayList");
    }

    public static Types getInstance() {
        if (instance == null) {
            instance = new Types();
        }
        return instance;
    }

    public boolean isPrimitive(Field type) {
        return primitives.contains(type.getType().getName());
    }
    

    public boolean isPrimitive(String type) {
        return primitives.contains(type);
    }
    
    public boolean isObject(Field type) {
    	return !Collection.class.isAssignableFrom(type.getType()) && type.toString().contains("java");
    }

    public boolean isListPrimitive(Field type) {
		ParameterizedType listType = (ParameterizedType)type.getGenericType();
        Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
        boolean primitive = listClass.isPrimitive() || listClass.getName().equals("java.lang.String");
        return Collection.class.isAssignableFrom(type.getType()) && primitive;
    }
    
    public boolean isListObject(Field type) {
		ParameterizedType listType = (ParameterizedType)type.getGenericType();
        Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
        boolean primitive = listClass.isPrimitive() || listClass.getName().equals("java.lang.String");
        return Collection.class.isAssignableFrom(type.getType()) && !primitive;
    }
}
