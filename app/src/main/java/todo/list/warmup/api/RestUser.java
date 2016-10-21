package todo.list.warmup.api;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jrvansuita on 21/10/16.
 */

public class RestUser extends  Rest {

    private static final String PATH = "/user";

    public RestUser newUser(String id) {
        INewUser s = Rest.build().create(INewUser.class);

        Call<ResponseBody> call = s.auth(Rest.getUserRequestBody(id));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() == null || response.code() != 200) {
                        this.onFailure(call, new Throwable(response.message()));
                    } else {
                        if (onSucess != null)
                            onSucess.onSucess(response.body().string());
                    }
                } catch (IOException e) {
                    this.onFailure(call, e.getCause());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (onError != null)
                    onError.onError(t.getMessage());
            }
        });

        return this;
    }

    public static RestUser get() {
        return new RestUser();
    }

    public interface INewUser {
        @POST(PATH)
        Call<ResponseBody> auth(@Body RequestBody body);
    }


    public interface OnSucess {
        void onSucess(String s);
    }

    public interface OnError {
        void onError(String s);
    }


    protected OnSucess onSucess;
    protected OnError onError;

    public RestUser setOnSucess(OnSucess onSucess) {
        this.onSucess = onSucess;
        return this;
    }

    public RestUser setOnError(OnError onError) {
        this.onError = onError;
        return this;
    }
}
