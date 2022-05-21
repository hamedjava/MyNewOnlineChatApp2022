package com.example.newonlinechatapp2022;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdateProfileActivity extends AppCompatActivity {

    private CardView cv_updateUserImage;
    private ImageView img_updateUserImage;
    private EditText edt_UpdateUsername;
    private Toolbar toolbar;
    private ImageButton btn_back_updateProfile;
    private Button btn_updateProfile;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ProgressBar progressBar;
    private Uri imagePath;
    Intent intent;

    private static final int PICK_IMAGE = 521;
    String newName;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        setupViews();
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        intent = getIntent();

        btn_back_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




        btn_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UpdateProfileActivity.this, "Update Your Profile...", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setupViews(){
        edt_UpdateUsername = (EditText)findViewById(R.id.edt_updateUsername);
        btn_back_updateProfile = (ImageButton)findViewById(R.id.btn_back_updateProfile);
        btn_updateProfile = (Button)findViewById(R.id.btn_updateProfile);
        img_updateUserImage = (ImageView)findViewById(R.id.img_updateProfileUser);
        toolbar = (Toolbar)findViewById(R.id.updateProfile_toolbar);
        cv_updateUserImage = (CardView) findViewById(R.id.cv_updateUserImage);
        progressBar = (ProgressBar)findViewById(R.id.updateProfileProgressbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data != null){
            imagePath = data.getData();
            img_updateUserImage.setImageURI(imagePath);

        }else{
            Toast.makeText(this, "Please Select An Image", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
