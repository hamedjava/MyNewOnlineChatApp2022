package com.example.newonlinechatapp2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class OTBAuthenticationActivity extends AppCompatActivity {

    TextView tv_otb_changeNumber;
    EditText edt_getOTB;
    Button btn_OTB_auth_verify;
    FirebaseAuth firebaseAuth;
    ProgressBar OTB_progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otbauthentication);

        tv_otb_changeNumber = (TextView)findViewById(R.id.tv_OTB_help_to_receiveNumber);
        OTB_progressBar = (ProgressBar)findViewById(R.id.OTB_authentication_progressBar);
        btn_OTB_auth_verify = (Button)findViewById(R.id.btn_OTB_authentication);
        firebaseAuth = FirebaseAuth.getInstance();

        tv_otb_changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OTBAuthenticationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        btn_OTB_auth_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });


    }
}