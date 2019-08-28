package hr.branimir.barun.justquiz.question;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import hr.branimir.barun.justquiz.api.Result;

public class Question implements  Serializable {

    public transient Context context;
    public List<Result> result;

    public String question;

    public String optA;
    public String optB;
    public String optC;
    public String optD;
    public Integer answer;

    public Question() {

    }

    public Question(Context context) {
        this.context = context;
        String question;
        Result result;
        String optA;
        String optC;
        String optD;
        Integer answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptA() {
        return optA;
    }

    public String getOptB() {
        return optB;
    }

    public String getOptC() {
        return optC;
    }

    public String getOptD() {
        return optD;
    }
}

