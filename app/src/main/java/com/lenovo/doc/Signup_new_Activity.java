package com.lenovo.doc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Signup_new_Activity extends AppCompatActivity {

    private EditText mobile;
    //private EditText pass;
    private Button signup;
    private FirebaseAuth mAuth;
    private String codeSent;
    private Button btn;
    private TextView no,cr;
    private EditText otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_new_);

        mobile=(EditText)findViewById(R.id.signin_contact_new);
        //pass=(EditText)findViewById(R.id.signin_password_new);
        signup=(Button)findViewById(R.id.login_btn_new);
        btn=(Button)findViewById(R.id.otp_btn);
        cr=(TextView)findViewById(R.id.create_text);
        no=(TextView)findViewById(R.id.otp_text);
        mAuth=FirebaseAuth.getInstance();
        otp=(EditText)findViewById(R.id.otp);
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });*/
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signupfn();
                String mobile2 = mobile.getText().toString().trim();

                if(mobile2.isEmpty() || mobile.length() < 10){
                    mobile.setError("Enter a valid mobile");
                    mobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(Signup_new_Activity.this, verifyOTP.class);
                intent.putExtra("mobile", mobile2);
                startActivity(intent);
            }
        });
    }
    private void signupfn(){
        String ph=mobile.getText().toString();
        if(TextUtils.isEmpty(ph)){
            mobile.setError("Phone no is required");
            mobile.requestFocus();
            return;
        }
        /*String pas=pass.getText().toString();
        if(TextUtils.isEmpty(pas)){
            pass.setError("Email is required");
            pass.requestFocus();
            return;
        }*/
        if(!Patterns.PHONE.matcher(ph).matches()){
            mobile.setError("Enter correcct phone no");
            mobile.requestFocus();
            return;
        }
        /*if(pas.length()<6){
            pass.setError("Password length should be more than 6");
            pass.requestFocus();
            return;
        }*/
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+ph,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        //Intent intent=new Intent(Signup_new_Activity.this,verifyOTP.class);
        //intent.putExtra("OTP",codeSent);
        //startActivity(intent);
        cr.setVisibility(View.GONE);
        mobile.setVisibility(View.GONE);
        //pass.setVisibility(View.GONE);
        signup.setVisibility(View.GONE);
        btn.setVisibility(View.VISIBLE);
        no.setVisibility(View.VISIBLE);
        cr.setVisibility(View.VISIBLE);
        otp.setVisibility(View.VISIBLE);


    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent=s;
            Toast.makeText(Signup_new_Activity.this, s, Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyOtp(){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(codeSent,otp.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(Signup_new_Activity.this,WelcomeActivity.class));
                            finish();
                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                String error=task.getException().getMessage();
                                Toast.makeText(Signup_new_Activity.this, error, Toast.LENGTH_SHORT).show();
                                // The verification code entered was invalid
                            }
                            else{
                                String error=task.getException().getMessage();
                                Toast.makeText(Signup_new_Activity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
