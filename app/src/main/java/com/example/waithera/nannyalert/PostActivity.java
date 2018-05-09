package com.example.waithera.nannyalert;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class PostActivity extends AppCompatActivity {
private static final int GALLERY_REQUEST=2;
//uri stores image
private Uri uri=null;
private ImageButton imageButton;
private EditText editName;
private EditText editDesc;
private EditText oBnumber;
private StorageReference storageReference;
private FirebaseDatabase database;
private DatabaseReference databaseReference;
private FirebaseStorage storage;
private FirebaseAuth mAuth;
private DatabaseReference mDatabaseUsers;
private FirebaseUser mCurrentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        editName=(EditText)findViewById(R.id.editName);
        editDesc=(EditText)findViewById(R.id.editDesc);
        oBnumber=(EditText)findViewById(R.id.obNumber);
        storageReference= storage.getInstance().getReference();
        databaseReference=database.getInstance().getReference().child("NannyAlert");

        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
    }
        //getting access to gallery

    public void imageButtonClicked(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);

    }
    //code to capture upload image once selected
@Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            uri = data.getData();
            imageButton = (ImageButton) findViewById(R.id.imageButton);
            imageButton.setImageURI(uri);

        }

    }
    //code to submit image and data to firebase
    public void submitButtonClicked(View view){
        final String nameValue=editName.getText().toString().trim();
        final String description=editDesc.getText().toString().trim();
        final String occurenceBk=oBnumber.getText().toString().trim();
        if(!TextUtils.isEmpty(nameValue)&&!TextUtils.isEmpty(description) &&!TextUtils.isEmpty(occurenceBk)){
          //StorageReference filepath=storageReference.child("PostImage").child(uri.getLastPathSegment());
          StorageReference filepath=storageReference.child("images/"+ UUID.randomUUID().toString());
            Toast.makeText(PostActivity.this,"Submitting....",Toast.LENGTH_LONG).show();
           filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final  Uri downloadUrl=taskSnapshot.getDownloadUrl();
                   Toast.makeText(PostActivity.this,"Upload complete",Toast.LENGTH_LONG).show();
             final DatabaseReference newPost=databaseReference.push();

              mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      newPost.child("name").setValue(nameValue);
                      newPost.child("desc").setValue(description);
                      newPost.child("image").setValue(downloadUrl.toString());
                      newPost.child("obNumber").setValue(occurenceBk);
                      newPost.child("uid").setValue(mCurrentUser.getUid());
                      newPost.child("username").setValue(dataSnapshot.child("username").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {

                              if(task.isSuccessful()){
                                 Intent casesActivityIntent= new Intent(PostActivity.this,cases.class);
                            startActivity(casesActivityIntent);
                             }
                          }
                      });
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });

               }
           })
             .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(PostActivity.this,"Unable to upload",Toast.LENGTH_LONG).show();
                    // Handle unsuccessful uploads
                    // ...
                }
            });

        }
    }
}
