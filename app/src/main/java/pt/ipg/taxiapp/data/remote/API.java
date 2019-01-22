package pt.ipg.taxiapp.data.remote;


import pt.ipg.taxiapp.data.model.remote.DefaultResponse;
import pt.ipg.taxiapp.data.model.remote.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface API {

    //https://workshop-ipg.azurewebsites.net/explorer/

    @FormUrlEncoded
    @POST("API/Users")
    Call<DefaultResponse> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username

    );


    @FormUrlEncoded
    @POST("API/Users/login?include=user")
    Call<LoginResponse> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @PUT("updateuser/{id}")
    Call<LoginResponse> updateUser(
            @Path("id") int id,
            @Field("email") String email,
            @Field("name") String name
    );


}