package com.lenovo.doc;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

public class verifyOTP extends AppCompatActivity {
    private int STORAGE_PERMISSIN_CODE=1;
    private FirebaseAuth mAuth;
    private Button btn;
    private TextView no;
    private ProgressDialog dialog;
    private EditText otp;
    private String so;
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        Intent intent=getIntent();
        so=intent.getStringExtra("mobile");
        dialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        btn=(Button)findViewById(R.id.otp_btn);
        no=(TextView)findViewById(R.id.otp_text);
        otp=(EditText)findViewById(R.id.otp);
        if (ContextCompat.checkSelfPermission(verifyOTP.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestMessagePermission();
            // Permission is not granted
        }
        //sendVerificationCode(so);
        //startPhoneNumberVerification(so);
        mCallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId=s;
                Toast.makeText(verifyOTP.this, "code sent to user", Toast.LENGTH_SHORT).show();
            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + so,
                60,
                TimeUnit.SECONDS,
                this,
                mCallback);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otp.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    otp.setError("Enter valid code");
                    otp.requestFocus();
                    return;
                }
                verify(v);
                //verifying the code entered manually
                //verifyVerificationCode(code);
                //verifyPhoneNumberWithCode(mVerificationId,code);
            }
        });
    }
    private void requestMessagePermission(){
        if (ContextCompat.checkSelfPermission(verifyOTP.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(verifyOTP.this,
                    Manifest.permission.READ_CONTACTS)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Needed")
                        .setMessage("DOCMED requires to send you message. Allow us to do so..")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(verifyOTP.this,
                                        new String[]{Manifest.permission.READ_CONTACTS},
                                        STORAGE_PERMISSIN_CODE);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(verifyOTP.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        STORAGE_PERMISSIN_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            return;
            // Permission has already been granted
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if(requestCode==STORAGE_PERMISSIN_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*public void send_sms(View v){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + so,
                60,
                TimeUnit.SECONDS,
                this,
                mCallback);
    }*/
    public void signin_withphone(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(verifyOTP.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(verifyOTP.this, WelcomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                        dialog.dismiss();
                    }
                });
    }
    public void verify(View v){
        String input_code=otp.getText().toString();
        verify_phone_no(mVerificationId,input_code);
        Toast.makeText(this, "verifying.. ", Toast.LENGTH_SHORT).show();
        /*if(mVerificationId.equals("")){
            verify_phone_no(mVerificationId,input_code);
        }*/
    }
    private void verify_phone_no(String verify_code,String input_code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verify_code, input_code);
        dialog.setMessage("Verifying User...");
        dialog.show();
        //signing the user
        signin_withphone(credential);
    }




    /*private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            //Toast.makeText(verifyOTP.this, code, Toast.LENGTH_SHORT).show();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otp.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(verifyOTP.this, e.getMessage()+"  93", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
            Toast.makeText(verifyOTP.this, s+"  "+so+"  103", Toast.LENGTH_SHORT).show();
        }
    };
    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(verifyOTP.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(verifyOTP.this, WelcomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }*/


}
