package hr.branimir.barun.justquiz.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ApiQuestion {

    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("results")
    @Expose
    private List<Result> result = null;


    public ApiQuestion() {
    }

    public ApiQuestion(Integer responseCode, List<Result> result) {
        super();
        this.responseCode = responseCode;
        this.result = result;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public List<Result> getResult() {
        return result;
    }

}