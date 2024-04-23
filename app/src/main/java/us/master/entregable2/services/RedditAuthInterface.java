package us.master.entregable2.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RedditAuthInterface {
    @FormUrlEncoded
    @POST("api/v1/access_token")
    Call<ResponseBody> getAccessToken(
            @Header("Authorization") String authorization,
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password
    );
}
