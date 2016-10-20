package todo.list.warmup.bean;

/**
 * Created by jrvansuita on 19/10/16.
 */

public class ToDoList {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    public ToDoList(String name) {
        this.name = name;
    }
}
