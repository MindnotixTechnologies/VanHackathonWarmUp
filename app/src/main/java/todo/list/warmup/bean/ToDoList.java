package todo.list.warmup.bean;

/**
 * Created by jrvansuita on 19/10/16.
 */

public class ToDoList {
    private int id;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ToDoList(String title) {
        this.title = title;
    }

    public ToDoList(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
