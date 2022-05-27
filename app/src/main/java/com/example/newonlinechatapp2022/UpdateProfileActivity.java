package com.example.newonlinechatapp2022;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    private DatabaseReference databaseReference;
    private String ImageUriAccessToken;
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
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        intent = getIntent();

        btn_back_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());


        btn_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newName = edt_UpdateUsername.getText().toString();
                
                if(newName.isEmpty()){
                    Toast.makeText(UpdateProfileActivity.this, "Name Is Empty", Toast.LENGTH_SHORT).show();
                }else if(imagePath != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    UserProfile userProfile = new UserProfile(newName,firebaseAuth.getUid());
                    databaseReference.setValue(userProfile);

                    updateImageToStorage();
                    Toast.makeText(UpdateProfileActivity.this, "Updated....", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(UpdateProfileActivity.this,ChatActivity.class);
                    startActivity(intent);

                    finish();


                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    UserProfile userProfile = new UserProfile(newName,firebaseAuth.getUid());
                    databaseReference.setValue(userProfile);

                    updateNameOnCloudFirestore();
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(UpdateProfileActivity.this,ChatActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        img_updateUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });

        storageReference = firebaseStorage.getReference();
        storageReference.child("Image").child(firebaseAuth.getUid()).child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageUriAccessToken = uri.toString();
                Picasso.get().load(uri).into(img_updateUserImage);
            }
        });
    }

    private void updateNameOnCloudFirestore() {
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String , Object> userData = new HashMap<>();

        userData.put("name",newName);
        userData.put("image",ImageUriAccessToken);
        userData.put("uid",firebaseAuth.getUid());
        userData.put("Status","Online");

        documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UpdateProfileActivity.this, "Profile Update Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(UpdateProfileActivity.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateImageToStorage() {
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
                        ImageUriAccessToken=uri.toString();
                        Toast.makeText(UpdateProfileActivity.this, "URI get Success..", Toast.LENGTH_SHORT).show();
                        updateNameOnCloudFirestore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(UpdateProfileActivity.this, "URI get Failure..", Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(UpdateProfileActivity.this, "Image Is Uploaded", Toast.LENGTH_SHORT).show();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(UpdateProfileActivity.this, "Image Not Uploaded", Toast.LENGTH_SHORT).show();
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
