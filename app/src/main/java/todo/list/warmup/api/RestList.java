package todo.list.warmup.api;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import todo.list.warmup.bean.ToDoList;
import todo.list.warmup.pack.Bundle;
import todo.list.warmup.pack.Pack;
import todo.list.warmup.pack.Single;

/**
 * Created by jrvansuita on 21/10/16.
 */

public class RestList {


    public static void getAll(final IOnGetAll listener) {
        ILists s = Rest.build().create(ILists.class);

        Call<Pack<ToDoList>> call = s.getAll();
        call.enqueue(new Callback<Pack<ToDoList>>() {
            @Override
            public void onResponse(Call<Pack<ToDoList>> call, Response<Pack<ToDoList>> response) {
                if (listener != null) {

                    List<ToDoList> list = new ArrayList();
                    for (Bundle<ToDoList> i : response.body().getData()) {
                        list.add(i.getAttributes());
                    }
                    listener.onGetAll(list);
                }
            }

            @Override
            public void onFailure(Call<Pack<ToDoList>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void newList(String title, final IOnNewOne listener) {
        ILists s = Rest.build().create(ILists.class);

        Call<Single<ToDoList>> call = s.newList(new Title(title));

        call.enqueue(new Callback<Single<ToDoList>>() {
            @Override
            public void onResponse(Call<Single<ToDoList>> call, Response<Single<ToDoList>> response) {
                if (listener != null) {
                    listener.onNewOne(response.body().getData().getAttributes());
                }
            }

            @Override
            public void onFailure(Call<Single<ToDoList>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void delete(final int id) {
        ILists s = Rest.build().create(ILists.class);

        Call<ResponseBody> call = s.delete(id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public interface ILists {
        @GET("/api/lists")
        Call<Pack<ToDoList>> getAll();

        @POST("/api/list")
        Call<Single<ToDoList>> newList(@Body Title title);

        @DELETE("/api/list/{id}")
        Call<ResponseBody> delete(@Path("id") int listId);

    }

    public interface IOnGetAll {
        void onGetAll(List<ToDoList> all);
    }

    public interface IOnNewOne {
        void onNewOne(ToDoList one);
    }

    private static class Title {
        private String title;

        public Title(String title) {
            this.title = title;
        }
    }
}
