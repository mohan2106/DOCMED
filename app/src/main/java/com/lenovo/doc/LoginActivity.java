package com.lenovo.doc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private ImageView img1,img2,img3;
    private EditText userName,userPassword;
    private FirebaseAuth mAuth;
    private Button signinBtn;
    private ProgressDialog progressDialog;
    private TextView for_pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        img1=(ImageView)findViewById(R.id.imageView2);
        img2=(ImageView)findViewById(R.id.imageView3);
        img3=(ImageView)findViewById(R.id.imageView4);
        userName=(EditText)findViewById(R.id.signin_email);
        for_pass=(TextView)findViewById(R.id.forgot_password);
        userPassword=(EditText)findViewById(R.id.signin_password);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        signinBtn=(Button)findViewById(R.id.signinButton);

        for_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,forgetPassword.class));
            }
        });

        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(LoginActivity.this,WelcomeActivity.class));
        }

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });



        findViewById(R.id.signUp_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });


    }
    private void login(){
        String email=userName.getText().toString();
        String password=userPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            userName.setError("Email is required");
            userName.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userName.setError("Enter correcct email");
            userName.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password)){
            userPassword.setError("Password is required");
            userPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            userPassword.setError("Password length should be greater then 6");
            userPassword.requestFocus();
            return;
        }
        progressDialog.setMessage("Logging you in...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(LoginActivity.this,WelcomeActivity.class));
                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
