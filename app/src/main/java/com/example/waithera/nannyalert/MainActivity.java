package com.example.waithera.nannyalert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
private EditText userName;
    private EditText editTextPassword;
    private Button logiN;
    private TextView registeR;

private ProgressDialog progressDialog;

private FirebaseAuth mfirebaseAuth;
private DatabaseReference mdatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//initialize
        mfirebaseAuth=FirebaseAuth.getInstance();
        mdatabaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
     /*   if(mfirebaseAuth.getCurrentUser() !=null){
            finish();
            startActivity(new Intent(getApplicationContext(),Login.class));
        }*/

        userName=(EditText)findViewById(R.id.username);
        editTextPassword=(EditText)findViewById(R.id.password);

        progressDialog=new ProgressDialog(this);

        logiN=(Button)findViewById(R.id.login);
        registeR=(TextView)findViewById(R.id.register);

logiN.setOnClickListener(this);
registeR.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v==logiN){
           //user will open login here
            userLogin();
        }
        if(v==registeR){
            finish();
           Intent i=new Intent(this,signUp.class);
           startActivity(i);
        }
    }


    public void userLogin(){
        String email=userName.getText().toString().trim();
        String passWord=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter valid email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(passWord)){
            Toast.makeText(this,"Please enter valid password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Login in please wait...");
        progressDialog.show();

        mfirebaseAuth.signInWithEmailAndPassword(email,passWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                     if(task.isSuccessful()){
                         progressDialog.dismiss();
                        checkUserExists();
                     }
                     else{
                         Toast.makeText(MainActivity.this,"Ensure correct email and password",Toast.LENGTH_SHORT).show();
                         progressDialog.dismiss();
                     }
                    }
                });

    }
    public void checkUserExists(){
        final String user_id=mfirebaseAuth.getCurrentUser().getUid();
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              if(dataSnapshot.hasChild(user_id)){
                  Intent loginIntent=new Intent(MainActivity.this,Login.class);
                  //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(loginIntent);
              }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
