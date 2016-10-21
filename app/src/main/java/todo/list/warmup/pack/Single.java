package todo.list.warmup.pack;

/**
 * Created by jrvansuita on 21/10/16.
 */

public class Single<T> {

    private Bundle<T> data;

    public Bundle<T> getData() {
        return data;
    }

    public void setData(Bundle<T> data) {
        this.data = data;
    }

    public Single(Bundle<T> data) {
        this.data = data;
    }
}
