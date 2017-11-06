package bankzworld.com.utilities;

import bankzworld.com.model.TrailerResponse;
import bankzworld.com.model.movieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by King Jaycee on 26/10/2017.
 */

public interface APiService {

    @GET("popular")
    Call<movieResponse> getPopular(@Query("api_key") String api_key);

    @GET("top_rated")
    Call<movieResponse> getTopRated(@Query("api_key") String api_key);

    @GET("{id}/videos?")
    Call<TrailerResponse> getTrailerKey(@Path("id") String id, @Query("api_key") String api_key);

    @GET("{id}/reviews?")
    Call<TrailerResponse> getMovieReview(@Path("id") String id, @Query("api_key") String api_key);

}
