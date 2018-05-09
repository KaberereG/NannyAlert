package com.example.waithera.nannyalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdviceActivity extends AppCompatActivity {
private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);

        back = (Button) findViewById(R.id.back_button);

    }
    public void backButtonClicked(View view){
        startActivity(new Intent(this,Login.class));
    }
}
