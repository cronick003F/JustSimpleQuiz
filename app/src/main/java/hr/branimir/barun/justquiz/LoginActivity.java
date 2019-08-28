package hr.branimir.barun.justquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_email_login)
    EditText etEmailLogin;
    @BindView(R.id.et_password_login)
    EditText etPasswordLogin;
    @BindView(R.id.btn_login_login)
    Button btnLoginLogin;
    @BindView(R.id.btn_signup_login)
    Button btnSignupLogin;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

    }


    @OnClick({R.id.btn_login_login, R.id.btn_signup_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login_login:
                String email = etEmailLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();
                if (!email.equalsIgnoreCase("")) {
                    if (!password.equalsIgnoreCase("")) {

                        loginUser(email,password);

                    } else {
                        Toast.makeText(this,"Please enter password.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(this,"Please enter email.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_signup_login:
                Intent intentSignup = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intentSignup);
                break;
        }
    }

    public void loginUser(String email, String password) {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login successful!",Toast.LENGTH_LONG).show();
                    Intent homeIntent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                    finish();
                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(LoginActivity.this,""+errorMessage, Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}





