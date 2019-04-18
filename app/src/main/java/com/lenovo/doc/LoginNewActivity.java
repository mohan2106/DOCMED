package com.lenovo.doc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginNewActivity extends AppCompatActivity {

    private EditText mobile,pass;
    private Button signin;
    private TextView forgot_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        mobile=(EditText)findViewById(R.id.signin_contact_new);
        pass=(EditText)findViewById(R.id.signin_password_new);
        signin=(Button)findViewById(R.id.login_btn_new);
        forgot_pass=(TextView)findViewById(R.id.forgot_password_new);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
