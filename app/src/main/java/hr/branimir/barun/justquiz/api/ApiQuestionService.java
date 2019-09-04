package hr.branimir.barun.justquiz.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiQuestionService {

    String BASE_URL = "https://opentdb.com/";

    @GET("api.php")
    Call<ApiQuestion> getQuizQuestions(
            @Query("amount") Integer amount,
            @Query("type") String type,
            @Query("encode") String encode);
}
