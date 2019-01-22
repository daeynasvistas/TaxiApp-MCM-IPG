package pt.ipg.taxiapp.data.remote;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pt.ipg.taxiapp.data.persistance.local.PrefManager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    //private static final String AUTH = "Basic ";
    private static final String BASE_URL = "https://workshop-ipg.azurewebsites.net/"; // <--- ALTERAR vers.0.5
    private static Client mInstance;
    private Retrofit retrofit;


    private Client(final String token) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                Request.Builder requestBuilder = original.newBuilder()
                                        .addHeader("Authorization", token)
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        }
                ).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static synchronized Client getInstance(String token) {
        if (mInstance == null) {
            mInstance = new Client(token);
        }
        return mInstance;
    }

    public API getApi() {
        return retrofit.create(API.class);
    }
}
