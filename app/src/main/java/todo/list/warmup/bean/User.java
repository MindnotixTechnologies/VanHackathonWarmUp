package todo.list.warmup.bean;

/**
 * Created by jrvansuita on 21/10/16.
 */

public class User {
    private String firebase_key;

    public String getFirebase_key() {
        return firebase_key;
    }

    public void setFirebase_key(String firebase_key) {
        this.firebase_key = firebase_key;
    }

    public User(String firebase_key) {
        this.firebase_key = firebase_key;
    }
}
