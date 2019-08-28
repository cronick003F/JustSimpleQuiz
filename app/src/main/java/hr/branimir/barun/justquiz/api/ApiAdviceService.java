package hr.branimir.barun.justquiz.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiAdviceService {

    String URL = "https://api.adviceslip.com";

    @GET("/advice")
    Call<ApiAdvice> getAdviceSlip();
}
