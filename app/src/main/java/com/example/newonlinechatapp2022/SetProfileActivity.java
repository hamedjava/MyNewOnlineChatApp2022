package com.example.newonlinechatapp2022;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SetProfileActivity extends AppCompatActivity {

    private CardView cv_getUserImage;
    ImageView img_getUserImage;
    private static final int PICK_IMAGE = 997;
    private Uri imagePath;
    private EditText edt_getUsername;
    Button btn_saveProfile;
    ProgressBar setProfile_progressBar;



    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String name;
    private String uriImageAccessToken;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        edt_getUsername = (EditText)findViewById(R.id.edt_getUsername);
        img_getUserImage = (ImageView)findViewById(R.id.img_profile_logo);
        cv_getUserImage = (CardView)findViewById(R.id.cv_getUserImage);
        setProfile_progressBar = (ProgressBar)findViewById(R.id.saveProfileProgressBar);
        btn_saveProfile = (Button)findViewById(R.id.btn_saveProfile);

        cv_getUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);

            }
        });

        btn_saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edt_getUsername.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(SetProfileActivity.this, "Name Is Empty", Toast.LENGTH_SHORT).show();
                }else if(imagePath == null){
                    Toast.makeText(SetProfileActivity.this, "Image Is Empty", Toast.LENGTH_SHORT).show();
                }else{
                    setProfile_progressBar.setVisibility(View.VISIBLE);
                    sendDataForNewUser();
                    setProfile_progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(SetProfileActivity.this,ChatActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void sendDataForNewUser() {
        sendDataToRealTimeDatabase();
    }

    private void sendDataToRealTimeDatabase() {
        name = edt_getUsername.getText().toString().trim();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        UserProfile userProfile = new UserProfile(name,firebaseAuth.getUid());

        databaseReference.setValue(userProfile);
        Toast.makeText(this, "User Added Successful ", Toast.LENGTH_SHORT).show();

        sendImageToStorage();

    }

    private void sendImageToStorage() {
        StorageReference imageRef = storageReference.child("Image").child(firebaseAuth.getUid()).child("Profile Pic");

        //Image Compression
        Bitmap bitmap = null;
        try{
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
        }catch(IOException e){
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ////Putting Image To Storage
        UploadTask uploadTask = imageRef.putBytes(bytes);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uriImageAccessToken=uri.toString();
                        Toast.makeText(SetProfileActivity.this, "URI get Success..", Toast.LENGTH_SHORT).show();
                        sendDataToCloudFirestore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(SetProfileActivity.this, "URI get Failure..", Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(SetProfileActivity.this, "Image Is Uploading", Toast.LENGTH_SHORT).show();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(SetProfileActivity.this, "Image Not Upload", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void sendDataToCloudFirestore() {
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String , Object> userData = new HashMap<>();

        userData.put("name",name);
        userData.put("image",uriImageAccessToken);
        userData.put("uid",firebaseAuth.getUid());
        userData.put("Status","Online");

        documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SetProfileActivity.this, "Data Send On Cloud Firestore Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(SetProfileActivity.this, "Data Send On Cloud Firestore Failed...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data != null){
            imagePath = data.getData();
            img_getUserImage.setImageURI(imagePath);

        }else{
            Toast.makeText(this, "Please Select An Image", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}