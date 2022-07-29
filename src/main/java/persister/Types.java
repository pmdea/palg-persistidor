package persister;

import java.util.ArrayList;
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

    public boolean isPrimitive(String type) {
        return primitives.contains(type);
    }

    public boolean isList(String type) {
        return lists.contains(type);
    }
}
