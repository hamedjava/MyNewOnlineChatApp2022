package com.example.newonlinechatapp2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText edt_getCountryCode;
    CountryCodePicker countryCodePicker;
    Button btn_verify;

    String countryCode;
    String phoneNumber;

    FirebaseAuth firebaseAuth;

    ProgressBar progressBar;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    String codeSend;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        countryCodePicker = (CountryCodePicker)findViewById(R.id.countryCodePicker);
        btn_verify = (Button)findViewById(R.id.btn_main_verify);
        edt_getCountryCode = (EditText) findViewById(R.id.edt_main_getUserNumber);
        progressBar = (ProgressBar)findViewById(R.id.mainProgressBar);


        firebaseAuth = FirebaseAuth.getInstance();

        countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
            }
        });



        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = edt_getCountryCode.getText().toString();
                if(number.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter Your Number", Toast.LENGTH_SHORT).show();
                }else if(number.length() <= 10){
                    Toast.makeText(MainActivity.this, "Please Enter Correct Number", Toast.LENGTH_SHORT).show();
                }else{

                    progressBar.setVisibility(View.VISIBLE);
                    phoneNumber = countryCode+number;
                    Toast.makeText(MainActivity.this, phoneNumber+"", Toast.LENGTH_SHORT).show();

                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(mCallBack)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);

                }
            }
        });



        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                //how to automatically fetch code here
            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(MainActivity.this, "OTB Code Is Send", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                codeSend=s;
                Intent intent = new Intent(MainActivity.this,OTBAuthenticationActivity.class);
                intent.putExtra("otb",codeSend);
                startActivity(intent);
            }
        };



    }


    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(MainActivity.this,ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}