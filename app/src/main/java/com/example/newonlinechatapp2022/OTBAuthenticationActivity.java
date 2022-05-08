package com.example.newonlinechatapp2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

public class OTBAuthenticationActivity extends AppCompatActivity {

    TextView tv_otb_changeNumber;
    EditText edt_getOTB;
    Button btn_OTB_auth_verify;
    FirebaseAuth firebaseAuth;
    ProgressBar OTB_progressBar;

    String enteredOTB;

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
                enteredOTB = edt_getOTB.getText().toString();
                if(enteredOTB.isEmpty()){
                    Toast.makeText(OTBAuthenticationActivity.this, "Enter Your OTB First", Toast.LENGTH_SHORT).show();
                }else{
                    OTB_progressBar.setVisibility(View.VISIBLE);
                    String codeReceived = getIntent().getStringExtra("otb");

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeReceived,enteredOTB);

                    signInWithPhoneAuthCredential(credential);
                }
            }
        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    OTB_progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(OTBAuthenticationActivity.this, "LOGIN SUCCESS", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OTBAuthenticationActivity.this,SetProfileActivity.class);
                    startActivity(intent);
                    finish();
                }else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                    OTB_progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(OTBAuthenticationActivity.this, "LOGIN Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}