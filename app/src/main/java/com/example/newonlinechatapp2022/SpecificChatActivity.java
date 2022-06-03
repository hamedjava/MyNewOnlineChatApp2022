package com.example.newonlinechatapp2022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SpecificChatActivity extends AppCompatActivity {

    EditText edt_specific_sendMessage;
    ImageButton btn_send_message
                ,btn_specific_back;

    Toolbar toolbar;
    ImageView img_userImageViewOnToolbar;
    TextView tv_specific_nameOfUser;

    private String enteredMessage;
    Intent intent;

    private String receivedMessage
            ,senderName
            ,receiverUID
            ,senderUID;


    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    String senderRoom,ReceiverRoom;
    RecyclerView rv_messagesList;

    String currentTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_chat);
        setupViews();
        setSupportActionBar(toolbar);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        














        btn_specific_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SpecificChatActivity.this, "Toolbar Is Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setupViews(){
        edt_specific_sendMessage = (EditText) findViewById(R.id.edt_specific_getMessage);
        btn_send_message = (ImageButton)findViewById(R.id.btn_specific_sendMessage);
        toolbar = (Toolbar)findViewById(R.id.specific_chat_toolbar);
        img_userImageViewOnToolbar = (ImageView)findViewById(R.id.img_specificUserLogo);
        btn_specific_back = (ImageButton)findViewById(R.id.btn_back_specificChat);
        tv_specific_nameOfUser = (TextView) findViewById(R.id.tv_specific_nameOfUser);
        intent = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

    }
}