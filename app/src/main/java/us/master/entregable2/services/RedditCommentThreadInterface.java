package us.master.entregable2.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RedditCommentThreadInterface {
    @GET("r/{subreddit}/comments/{article}")
    Call<ResponseBody> getComments(@Header("Authorization") String authHeader, @Path("subreddit") String subreddit, @Path("article") String article, @Query("limit") int limit, @Query("depth") int depth);
}