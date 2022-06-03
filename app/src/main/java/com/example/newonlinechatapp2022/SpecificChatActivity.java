package com.example.newonlinechatapp2022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SpecificChatActivity extends AppCompatActivity {

    EditText edt_specific_sendMessage;
    ImageButton btn_send_message
                ,btn_specific_back;
    CardView cardView_sendMessage;
    Toolbar toolbar;
    ImageView img_userImageViewOnToolbar;
    TextView tv_specific_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_chat);
        setupViews();
        setSupportActionBar(toolbar);
    }

    public void setupViews(){
        edt_specific_sendMessage = (EditText) findViewById(R.id.edt_specific_getMessage);
        btn_send_message = (ImageButton)findViewById(R.id.btn_specific_sendMessage);
        toolbar = (Toolbar)findViewById(R.id.specific_chat_toolbar);
        img_userImageViewOnToolbar = (ImageView)findViewById(R.id.img_specificUserLogo);
        btn_specific_back = (ImageButton)findViewById(R.id.btn_back_specificChat);
    }
}