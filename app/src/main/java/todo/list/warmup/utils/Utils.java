package todo.list.warmup.utils;

/**
 * Created by jrvansuita on 20/10/16.
 */

public class Utils {


    public static String coalesce(String... array) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null && !array[i].isEmpty()) {
                    return array[i];
                }
            }
        }
        return null;
    }

}
