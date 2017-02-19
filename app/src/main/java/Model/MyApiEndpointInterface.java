package Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Tuan on 2017/02/19.
 */
public interface MyApiEndpointInterface {
    @GET("movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed")
    Call<ResultWrapper> getAllMovie();
    @GET("movie/{id}?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed")
    Call<MovieDetail> getMovieDetail(@Path("id") int id);
}
