package todo.list.warmup.api;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import todo.list.warmup.bean.User;

/**
 * Created by jrvansuita on 21/10/16.
 */

public abstract class Rest {

    private static final String URL = "https://todo-api-warmup.herokuapp.com/";

    protected static Retrofit build() {
        return new Retrofit.Builder().client(getHeader()).addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL).build();
    }


    private static OkHttpClient getHeader() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request.Builder requestBuilder = chain.request().newBuilder()
                                        .addHeader("firebase_key", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                return chain.proceed(requestBuilder.build());
                            }
                        })
                .build();
    }

    public static RequestBody getUserRequestBody(String id) {
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(new User(id)));
    }


}
