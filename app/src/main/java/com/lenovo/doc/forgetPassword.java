package com.lenovo.doc;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class forgetPassword extends AppCompatActivity {

    private EditText for_email;
    private Button for_password_btn;
    private TextView go_back;
    private FirebaseAuth mAuth;
    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailIconText;
    private ProgressBar emailIconProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        emailIconContainer=(ViewGroup)findViewById(R.id.linearLayoutForgetPassword);
        emailIcon=(ImageView)findViewById(R.id.forgot_password_email_icon);
        emailIconText=(TextView)findViewById(R.id.forget_password_email_text);
        emailIconProgress=(ProgressBar)findViewById(R.id.forget_password_progress_bar);
        for_email=(EditText)findViewById(R.id.forgot_email);
        for_password_btn=(Button)findViewById(R.id.forgot_button);
        mAuth=FirebaseAuth.getInstance();
        go_back=(TextView)findViewById(R.id.back_text);
        for_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(for_email.getText().toString());
            }
        });
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(forgetPassword.this,LoginActivity.class));
                finish();
            }
        });
    }
    private void sendEmail(String email){
        if(TextUtils.isEmpty(email)){
            for_email.setError("Enter your registered email..");
            for_email.requestFocus();
            return;
        }
        for_password_btn.setEnabled(false);
        TransitionManager.beginDelayedTransition(emailIconContainer);
        emailIcon.setVisibility(View.VISIBLE);
        emailIconProgress.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    ScaleAnimation scalenimation=new ScaleAnimation(1,0,1,0,emailIconProgress.getPivotX(),emailIconProgress.getPivotY());
                    scalenimation.setDuration(100);
                    scalenimation.setInterpolator(new AccelerateInterpolator());
                    scalenimation.setRepeatMode(Animation.REVERSE);
                    scalenimation.setRepeatCount(1);
                    scalenimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            emailIconProgress.setVisibility(View.INVISIBLE);
                            emailIcon.setColorFilter(getResources().getColor(R.color.colorAccent));
                            emailIconText.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    emailIcon.startAnimation(scalenimation);

                }
                else{
                    for_password_btn.setEnabled(true);
                    String error=task.getException().getMessage();
                    emailIconText.setText(error);
                    emailIconText.setTextColor(getResources().getColor(R.color.colorAccent));
                    TransitionManager.beginDelayedTransition(emailIconContainer);
                    emailIconProgress.setVisibility(View.GONE);
                    emailIconText.setVisibility(View.VISIBLE);
                    //Toast.makeText(forgetPassword.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
