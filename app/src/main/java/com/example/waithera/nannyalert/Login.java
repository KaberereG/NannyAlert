package com.example.waithera.nannyalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth firebaseAuth;
    private Button casesButton;
    private Button adviceButton;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();

            startActivity(new Intent(this, MainActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        casesButton = (Button) findViewById(R.id.cases);
        adviceButton=(Button)findViewById(R.id.advice);
        logout=(Button)findViewById(R.id.logout);
casesButton.setOnClickListener(this);
        adviceButton.setOnClickListener(this);
logout.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        if(v==casesButton){
            startActivity(new Intent(getApplicationContext(),cases.class));
        }
        if(v==adviceButton){
            startActivity(new Intent(this,AdviceActivity.class));
        }
        if(v==logout){
            firebaseAuth.signOut();
            finish();
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
        }

    }
}
