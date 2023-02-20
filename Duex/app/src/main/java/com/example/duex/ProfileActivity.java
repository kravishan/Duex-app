package com.example.duex;

import static com.google.firebase.firestore.model.mutation.Precondition.exists;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    EditText usernameEditeText;
    Button updateProfileButton;
    ImageView uimage;
    TextView helpCenterBtn;
    StorageReference storageReference;

    DatabaseReference databaseReference;

    String UserID="";
    Uri filepath;
    Bitmap bitmap;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        uimage=(ImageView)findViewById(R.id.uImage);
        helpCenterBtn = findViewById(R.id.helpCenter);

        usernameEditeText =(EditText)findViewById(R.id.uName);
        updateProfileButton =(Button) findViewById(R.id.btn_update);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = user.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        storageReference= FirebaseStorage.getInstance().getReference();

        helpCenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), accountHelp.class);
                startActivity(intent);
            }
        });

        uimage.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Dexter.withContext(getApplicationContext())
                                                  .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                                  .withListener(new PermissionListener() {
                                                      @Override
                                                      public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                                          Intent intent=new Intent();
                                                          intent.setType("image/*");
                                                          intent.setAction(Intent.ACTION_GET_CONTENT);
                                                          startActivityForResult(Intent.createChooser(intent,"Please Select File"),101);
                                                      }

                                                      @Override
                                                      public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                                      }

                                                      @Override
                                                      public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                                                      }
                                                  }).check();
                                      }
                                  });

                updateProfileButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateToFirebase();
                    }
                });
      }
      @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==101 && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            try {

                    InputStream inputStream=getContentResolver().openInputStream(filepath);
                    bitmap= BitmapFactory.decodeStream(inputStream);
                    uimage.setImageBitmap(bitmap);
                }catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
      }

    private void updateToFirebase() {
//        String username = usernameEditeText.getText().toString().trim();
//        databaseReference.child(UserID).child("username").setValue(username);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("File Uploader");
        pd.show();

        final StorageReference uploader = storageReference.child("profileimages/"+"img"+System.currentTimeMillis());
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Map<String, Object> map = new HashMap<>();
                                map.put("uimage", uri.toString());
                                map.put("username", usernameEditeText.getText().toString());


                                databaseReference.child(UserID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists())
                                            databaseReference.child(UserID).updateChildren(map);
                                        else
                                            databaseReference.child(UserID).setValue(map);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                                pd.dismiss();
                                Toast.makeText(ProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


    }
    @Override
        protected void onStart(){
        super.onStart();

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        UserID=user.getUid();
        databaseReference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usernameEditeText.setText(snapshot.child("username").getValue().toString());
                    Glide.with(getApplicationContext()).load(snapshot.child("uimage").getValue().toString()).into(uimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Database Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}