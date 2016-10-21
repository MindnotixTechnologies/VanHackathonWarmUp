package todo.list.warmup.api;

import java.io.IOException;
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
import retrofit2.http.PUT;
import retrofit2.http.Path;
import todo.list.warmup.bean.ToDoItem;
import todo.list.warmup.pack.Bundle;
import todo.list.warmup.pack.Pack;
import todo.list.warmup.pack.Single;

/**
 * Created by jrvansuita on 21/10/16.
 */

public class RestItem {


    public static void getAll(int listId, final IOnGetAll listener) {
        IItems s = Rest.build().create(IItems.class);

        Call<Pack<ToDoItem>> call = s.getAll(listId);
        call.enqueue(new Callback<Pack<ToDoItem>>() {
            @Override
            public void onResponse(Call<Pack<ToDoItem>> call, Response<Pack<ToDoItem>> response) {
                if (listener != null) {

                    List<ToDoItem> list = new ArrayList();
                    for (Bundle<ToDoItem> i : response.body().getData()) {
                        list.add(i.getAttributes());
                    }
                    listener.onGetAll(list);
                }
            }

            @Override
            public void onFailure(Call<Pack<ToDoItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void newItem(int listId, String description, final IOnNewOne listener) {
        IItems s = Rest.build().create(IItems.class);

        Call<Single<ToDoItem>> call = s.newItem(new RestItem.Item(description, listId));

        call.enqueue(new Callback<Single<ToDoItem>>() {
            @Override
            public void onResponse(Call<Single<ToDoItem>> call, Response<Single<ToDoItem>> response) {
                if (listener != null) {
                    listener.onNewOne(response.body().getData().getAttributes());
                }
            }

            @Override
            public void onFailure(Call<Single<ToDoItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void delete(final int id) {
        IItems s = Rest.build().create(IItems.class);

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

     public static void check(final int id) {
        IItems s = Rest.build().create(IItems.class);

        Call<ResponseBody> call = s.check(id);

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


    public static void update(ToDoItem item) {
        IItems s = Rest.build().create(IItems.class);

        Call<ResponseBody> call = s.update(item);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String s = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public interface IItems {
        @GET("/api/items/{id}")
        Call<Pack<ToDoItem>> getAll(@Path("id") int listId);

        @POST("/api/item")
        Call<Single<ToDoItem>> newItem(@Body RestItem.Item item);

        @DELETE("/api/item/{id}")
        Call<ResponseBody> delete(@Path("id") int listId);

        @PUT("/api/item/{id}/checked")
        Call<ResponseBody> check(@Path("id") int itemId);

        @PUT("/api/item")
        Call<ResponseBody> update(@Body ToDoItem item);
    }

    public interface IOnGetAll {
        void onGetAll(List<ToDoItem> all);
    }

    public interface IOnNewOne {
        void onNewOne(ToDoItem one);
    }

    public interface IOnOk {
        void onOk(int id);
    }

    private static class Item{
        private String description;
        private int list_id;

        public Item(String description, int list_id) {
            this.description = description;
            this.list_id = list_id;
        }
    }

}
