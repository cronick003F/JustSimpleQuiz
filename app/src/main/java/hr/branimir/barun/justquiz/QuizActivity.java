package hr.branimir.barun.justquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import hr.branimir.barun.justquiz.api.ApiQuestion;
import hr.branimir.barun.justquiz.api.ApiQuestionService;
import hr.branimir.barun.justquiz.api.Result;
import hr.branimir.barun.justquiz.question.Question;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizActivity extends AppCompatActivity {

    ProgressBar progressBar;
    public Question q;
    int ans;
    int answer;

    @BindView(R.id.question)
    TextView question;

    @BindView(R.id.optionA)
    RadioButton opA;

    @BindView(R.id.optionB)
    RadioButton opB;

    @BindView(R.id.optionC)
    RadioButton opC;

    @BindView(R.id.optionD)
    RadioButton opD;

    @BindView(R.id.options)
    RadioGroup optionsGroup;

    @BindView(R.id.submit)
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        question = findViewById(R.id.question);
        question.setText("Quiz");

        q = new Question(getApplicationContext());

        fetchApi();

        ans = 0;
        answer = 0;

    }

    public void setUpRadio(View view) {

        int selectedId = optionsGroup.getCheckedRadioButtonId();
        switch (selectedId) {
            case R.id.optionA:
                ans = 1;
                break;
            case R.id.optionB:
                ans = 2;
                break;
            case R.id.optionC:
                ans = 3;
                break;
            case R.id.optionD:
                ans = 4;
                break;
            default:
                ans = 0;
        }
        optionsGroup.clearCheck();

    }

    public void loadQuestion(Question q) {

        opA.setVisibility(View.VISIBLE);
        opB.setVisibility(View.VISIBLE);
        opC.setVisibility(View.VISIBLE);
        opD.setVisibility(View.VISIBLE);

        question.setText(q.getQuestion());
        opA.setText(q.getOptA());
        opB.setText(q.getOptB());
        opC.setText(q.getOptC());
        opD.setText(q.getOptD());

        Button button = findViewById(R.id.submit);
        button.setVisibility(View.VISIBLE);

    }

    public void checkScore() {
        if (q.answer == ans) {
          HomeActivity.score ++;

            new CDialog(this).createAlert("CORRECT!",
                    CDConstants.SUCCESS,
                    CDConstants.LARGE)
                    .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)
                    .setDuration(2000)
                    .setTextSize(CDConstants.LARGE_TEXT_SIZE)
                    .show();

            Runnable r = new Runnable() {
                @Override
                public void run(){
                    Intent intent = new Intent(QuizActivity.this, QuizActivity.class);
                    startActivity(intent);
                }
            };

            Handler h = new Handler();
            h.postDelayed(r, 3000);


        } else {

            new CDialog(this).createAlert("WRONG!",
                    CDConstants.ERROR,
                    CDConstants.LARGE)
                    .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)
                    .setDuration(2000)
                    .setTextSize(CDConstants.LARGE_TEXT_SIZE)
                    .show();

            Runnable r = new Runnable() {
                @Override
                public void run(){
                    Intent intent = new Intent(QuizActivity.this, EndActivity.class);
                    startActivity(intent);
                }
            };

            Handler h = new Handler();
            h.postDelayed(r, 3000);




        }

    }

    public void clickSubmit(View view) {
        setUpRadio(view);
        checkScore();

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back Press is not allowed", Toast.LENGTH_LONG).show();
    }

    public void fetchApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiQuestionService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiQuestionService apiQuestionService = retrofit.create(ApiQuestionService.class);
        Call<ApiQuestion> call = apiQuestionService.getQuizQuestions(1, "url3986");

        call.enqueue(new Callback<ApiQuestion>() {

            @Override
            public void onResponse(Call<ApiQuestion> call, Response<ApiQuestion> response) {

                Log.v("url----->", call.request().url().toString());

                ApiQuestion apiQuestion = response.body();

                if (apiQuestion.getResponseCode() == 0) {
                    q.result = apiQuestion.getResult();

                    if (q.result != null)
                        try {
                            q.question = java.net.URLDecoder.decode(q.result.get(0).getQuestion(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    Random random = new Random();
                    Integer ran = random.nextInt(4);

                    try {
                        setOptions(q.result.get(0), ran);
                    } catch (IndexOutOfBoundsException e) {
                        Log.v("Exception----->", e.toString());
                        Intent exIntent = new Intent(QuizActivity.this, QuizActivity.class);
                        startActivity(exIntent);
                    }

                    q.answer = (ran + 1);
                    Log.v("Answer---->", q.answer.toString());
                }
                loadQuestion(q);
            }

            @Override
            public void onFailure(Call<ApiQuestion> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


    }

    void setOptions(Result r, Integer ran) throws IndexOutOfBoundsException {
        List<String> wrong;

        switch (ran) {
            case 0:
                try {
                    q.optA = (java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                wrong = r.getIncorrectAnswers();
                try {
                    q.optB = (java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optC = (java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optD = (java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    q.optB = (java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                wrong = r.getIncorrectAnswers();
                try {
                    q.optA = (java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optC = (java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optD = (java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    q.optC = (java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                wrong = r.getIncorrectAnswers();
                try {
                    q.optA = (java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optB = (java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optD = (java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    q.optD = (java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                wrong = r.getIncorrectAnswers();
                try {
                    q.optA = (java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optB = (java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optC = (java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}


