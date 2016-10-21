package todo.list.warmup.pack;

/**
 * Created by jrvansuita on 21/10/16.
 */

public class Bundle<T> {

    private String id;
    private String type;
    private T attributes;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getAttributes() {
        return attributes;
    }

    public void setAttributes(T attributes) {
        this.attributes = attributes;
    }

    public Bundle(String id, String type, T attributes) {
        this.id = id;
        this.type = type;
        this.attributes = attributes;
    }
}
