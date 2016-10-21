package todo.list.warmup.bean;

/**
 * Created by jrvansuita on 19/10/16.
 */

public class ToDoItem {

    private int id = 0;
    private String description;
    private int list_id;
    private boolean checked = false;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public ToDoItem(String description) {
        this.description = description;
    }


    public int getList_id() {
        return list_id;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }
}
