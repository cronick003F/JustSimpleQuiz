package hr.branimir.barun.justquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EndActivity extends AppCompatActivity {

    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.btn_play_again)
    Button btnPlayAgain;
    @BindView(R.id.btn_exit)
    Button btnExit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        ButterKnife.bind(this);
        tvScore.setText("#");
        setScoreView(HomeActivity.score);


    }

    @OnClick({R.id.btn_play_again, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_play_again:
                HomeActivity.score = 0;
                Intent intent = new Intent(EndActivity.this, QuizActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_exit:
                finish();
                moveTaskToBack(true);
                break;
        }
    }

    public void setScoreView (Integer score) {
        String i =  score.toString();
        tvScore.setText(i);
    }







}

