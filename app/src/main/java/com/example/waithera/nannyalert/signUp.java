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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signUp extends AppCompatActivity implements View.OnClickListener {
private EditText email;
private EditText password;
private EditText username;
private Button register;
private Button back;

private ProgressDialog progressDialog;

private FirebaseAuth mfirebaseAuth;
private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mfirebaseAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        if(mfirebaseAuth.getCurrentUser() !=null ){
            //start the login activity
            finish();
            startActivity(new Intent(getApplicationContext(),Login.class));
        }

        progressDialog=new ProgressDialog(this);

        username=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);


        register=(Button)findViewById(R.id.register);
        back=(Button)findViewById(R.id.back);

        register.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
if(v==register){
    registerUser();
}
if(v==back){
    finish();
    Intent i=new Intent(this,MainActivity.class);
    startActivity(i);
}
    }
public void registerUser(){
   final String emaiL=email.getText().toString().trim();
   final String userName=username.getText().toString().trim();
    String passWord=password.getText().toString().trim();
    if(TextUtils.isEmpty(userName)){
        Toast.makeText(this,"Please enter your user name",Toast.LENGTH_SHORT).show();
        return;
    }
    if(TextUtils.isEmpty(emaiL)){
        Toast.makeText(this,"Please enter valid email",Toast.LENGTH_SHORT).show();
        return;
    }
    if(TextUtils.isEmpty(passWord)){
        Toast.makeText(this,"Please enter password that has 8 characters",Toast.LENGTH_SHORT).show();
        return;
    }





    progressDialog.setMessage("Registering User...");
    progressDialog.show();

    mfirebaseAuth.createUserWithEmailAndPassword(emaiL,passWord)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id=mfirebaseAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db=mDatabase.child(user_id);
                        current_user_db.child("email").setValue(emaiL);
                        current_user_db.child("username").setValue(userName);
                        current_user_db.child("image").setValue("default");

                        Toast.makeText(signUp.this,"Registered Succesfully",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        finish();
                        Intent loginIntent=new Intent(getApplicationContext(),Login.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                    }
                    else{
                         Toast.makeText(signUp.this,"Could not register...Please try again. Ensure valid email and 8 character long password",Toast.LENGTH_LONG).show();
                         progressDialog.dismiss();

                    }
                }
            });
}

}
