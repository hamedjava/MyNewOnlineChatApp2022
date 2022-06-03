package com.example.newonlinechatapp2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
            ,receiverName
            ,receiverUID
            ,senderUID;


    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    String senderRoom,receiverRoom;
    RecyclerView rv_messagesList;

    String currentTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;


    MessagesAdapter adapter;
    ArrayList<MessagesModel> messagesModelArrayList;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_chat);
        setupViews();

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rv_messagesList.setLayoutManager(linearLayoutManager);
        adapter = new MessagesAdapter(SpecificChatActivity.this,messagesModelArrayList);
        rv_messagesList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        setSupportActionBar(toolbar);


        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        DatabaseReference databaseReference = firebaseDatabase.getReference().child("chats").child(senderRoom).child("message");
        adapter = new MessagesAdapter(SpecificChatActivity.this,messagesModelArrayList);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                messagesModelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MessagesModel model = dataSnapshot.getValue(MessagesModel.class);
                    messagesModelArrayList.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(SpecificChatActivity.this, "Chat Is Canceled", Toast.LENGTH_SHORT).show();
            }
        });


        senderUID = firebaseAuth.getUid();
        receiverUID = getIntent().getStringExtra("receiveruid");
        receiverName = getIntent().getStringExtra("name");



        senderRoom = senderUID + receiverUID;
        receiverRoom = receiverUID + senderUID;


        tv_specific_nameOfUser.setText(receiverName);

        String uri = intent.getStringExtra("imageuri");

        if(uri.isEmpty()){
            Toast.makeText(this, "Null Is Received", Toast.LENGTH_SHORT).show();
        }else{
            Picasso.get().load(uri).into(img_userImageViewOnToolbar);
        }


        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enteredMessage = edt_specific_sendMessage.getText().toString();

                if(enteredMessage.isEmpty()){
                    Toast.makeText(SpecificChatActivity.this, "Nothing To Send", Toast.LENGTH_SHORT).show();
                }else{

                    Date date = new Date();
                    currentTime = simpleDateFormat.format(calendar.getTime());
                    MessagesModel messagesModel = new MessagesModel(enteredMessage,firebaseAuth.getUid(),date.getTime(),currentTime);
                    firebaseDatabase.getReference().child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .push()
                            .setValue(messagesModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            firebaseDatabase.getReference()
                                    .child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .push()
                                    .setValue(messagesModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(SpecificChatActivity.this, "Message Can Not Send", Toast.LENGTH_SHORT).show();
                        }
                    });

                    edt_specific_sendMessage.setText(null);


                }

            }
        });



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
        rv_messagesList = (RecyclerView) findViewById(R.id.rv_specificChat);
        messagesModelArrayList = new ArrayList<>();
        intent = getIntent();

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

}