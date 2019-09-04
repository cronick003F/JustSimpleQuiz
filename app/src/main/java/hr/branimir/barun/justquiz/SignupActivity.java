package hr.branimir.barun.justquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.branimir.barun.justquiz.user.User;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.et_username_signup)
    EditText etUsernameSignup;
    @BindView(R.id.et_email_signup)
    EditText etEmailSignup;
    @BindView(R.id.et_password_signup)
    EditText etPasswordSignup;
    @BindView(R.id.et_conf_password_signup)
    EditText etConfPasswordSignup;
    @BindView(R.id.btn_signuup_signup)
    Button btnSignuupSignup;
    @BindView(R.id.tv_already)
    TextView tvAlready;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("USERS");
        progressDialog = new ProgressDialog(this);


    }

    @OnClick({R.id.btn_signuup_signup, R.id.tv_already})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_signuup_signup:

                String name = etUsernameSignup.getText().toString();
                String email = etEmailSignup.getText().toString();
                String password = etPasswordSignup.getText().toString();
                String confirmPassword = etConfPasswordSignup.getText().toString();

                if (!name.equalsIgnoreCase(""))
                {
                    if (!email.equalsIgnoreCase(""))
                    {
                        if(!password.equalsIgnoreCase(""))
                        {
                            if(!confirmPassword.equalsIgnoreCase(""))
                            {
                                if(confirmPassword.equals(password)) {

                                    registerUser(name,email,password);

                                } else {
                                    Toast.makeText(this,"Password doesn't match!.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(this,"Please enter confirm password.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this,"Please enter password.", Toast.LENGTH_LONG).show();
                        }
                    } else{
                        Toast.makeText(this,"Please enter email.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this,"Please enter name.", Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.tv_already:
                Intent  alreadyIntent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(alreadyIntent);
                break;
        }
    }

    public void registerUser(String name, String email, String password) {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    String uid = currentUser.getUid();

                    User user = new User (name, email);

                    databaseUsers.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(SignupActivity.this,"Registration successful!", Toast.LENGTH_LONG).show();
                                Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(loginIntent);

                                finish();
                            } else {
                                progressDialog.dismiss();
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(SignupActivity.this,""+errorMessage, Toast.LENGTH_LONG).show();

                            }

                        }
                    });


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this,"Error registering user.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
