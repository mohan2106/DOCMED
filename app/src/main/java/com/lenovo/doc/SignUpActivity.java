package com.lenovo.doc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText email,password,phone,name,confirm_password;
    private TextView signIn_btn;
    private Button register_btn;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email=(EditText) findViewById(R.id.signUp_email);
        phone=(EditText)findViewById(R.id.signUp_phone);
        name=(EditText)findViewById(R.id.signUp_userName);
        password=(EditText)findViewById(R.id.signUp_password);
        signIn_btn=(TextView)findViewById(R.id.SignIn_text);
        confirm_password=(EditText)findViewById(R.id.signUp_conform_password);
        firebaseFirestore=FirebaseFirestore.getInstance();
        register_btn=(Button)findViewById(R.id.register_btn);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });
        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(SignUpActivity.this,WelcomeActivity.class));
        }
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser(){
        final String userEmail=email.getText().toString();
        final String userPhone=phone.getText().toString();
        final String username=name.getText().toString();
        final String userpassword=password.getText().toString();
        String userConfirmPassword=confirm_password.getText().toString();

        if(TextUtils.isEmpty(userEmail)){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            email.setError("please enter valid E-mail");
            email.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(userPhone)){
            phone.setError("Phone no is required");
            phone.requestFocus();
            return;
        }
        if(userPhone.length()<10){
            phone.setError("enter valid contact no");
            phone.requestFocus();
            return;
        }
        if(userpassword.length()<6){
            password.setError("minimum length of password should be 6");
            password.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(userpassword)){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if(!userConfirmPassword.equals(userpassword)){
            confirm_password.setError("password not matches with above password");
            confirm_password.requestFocus();
            return;
        }
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        register_btn.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(userEmail, userpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            String uid=mAuth.getUid();
                            /*firebaseDatabase.getReference("UserInformation").child(uid).child("email").setValue(userEmail);
                            firebaseDatabase.getReference("UserInformation").child(uid).child("phone").setValue(userPhone);
                            firebaseDatabase.getReference("UserInformation").child(uid).child("name").setValue(username);
                            firebaseDatabase.getReference("UserInformation").child(uid).child("password").setValue(userpassword);*/
                            Map<Object,String> userdata=new HashMap<>();
                            userdata.put("UserEmail",userEmail);
                            userdata.put("UserName",username);
                            userdata.put("UserPhone",userPhone);

                            firebaseFirestore.collection("USERS")
                                    .add(userdata)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(SignUpActivity.this, "Refisytered Successfully", Toast.LENGTH_SHORT).show();
                                                // Sign in success, update UI with the signed-in user's information
                                                Intent intent=new Intent(SignUpActivity.this,WelcomeActivity.class);
                                                finish();
                                                startActivity(intent);
                                            }
                                            else{
                                                String error=task.getException().getMessage();
                                                Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                        } else {
                            // If sign in fails, display a message to the user.
                            register_btn.setEnabled(true);
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}
