package hr.branimir.barun.justquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import hr.branimir.barun.justquiz.api.ApiAdvice;
import hr.branimir.barun.justquiz.api.ApiAdviceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeActivity extends AppCompatActivity {
    Button start;
    public static Integer score = 0;
    public static String advice;
    public ApiAdvice apiAdvice;

    @BindView(R.id.advice)
    TextView adviceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        start=findViewById(R.id.home_start);
        start.setOnClickListener(onClickListener);
        adviceView = findViewById(R.id.advice);
        adviceView.setText("ADVICE");

        fetchApi();

    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.home_start){
                Intent intent = new Intent(HomeActivity.this, QuizActivity.class);
                startActivity(intent);
                view.setClickable(false);

            }

        }
    };

    public void fetchApi () {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiAdviceService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiAdviceService api = retrofit.create(ApiAdviceService.class);

        Call<ApiAdvice> call = api.getAdviceSlip();

        call.enqueue(new Callback<ApiAdvice>() {
            @Override
            public void onResponse(Call<ApiAdvice> call, Response<ApiAdvice> response) {
                apiAdvice = response.body();
                advice = apiAdvice.getSlip().getAdvice();
                Log.v("advice-----", advice);
                setAdviceView(advice);

            }

            @Override
            public void onFailure(Call<ApiAdvice> call, Throwable t) {

            }
        });

    }

    public void setAdviceView (String advice) {
        adviceView.setText(advice);
    }


}