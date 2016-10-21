package todo.list.warmup.pack;

import java.util.List;

/**
 * Created by jrvansuita on 21/10/16.
 */

public class Pack<T> {

    private List<Bundle<T>> data;


    public List<Bundle<T>> getData() {
        return data;
    }

    public void setData(List<Bundle<T>> data) {
        this.data = data;
    }

    public Pack(List<Bundle<T>> data) {
        this.data = data;
    }
}
