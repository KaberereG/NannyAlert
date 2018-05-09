package com.example.waithera.nannyalert;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleNannyActivity extends AppCompatActivity {

    private String post_key = null;
    private DatabaseReference mDatabase;
    private ImageView singlePostImage;
    private TextView singlePostNannyName;
    private TextView singlePostObNumber;
    private TextView singlePostDesc;
    private Button deleteButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_nanny);

        post_key = getIntent().getExtras().getString("Postid");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("NannyAlert");

        singlePostDesc=(TextView)findViewById(R.id.singleDesc);
        singlePostNannyName=(TextView)findViewById(R.id.singlenannyname);
        singlePostObNumber=(TextView)findViewById(R.id.singleobnumber);
        singlePostImage=(ImageView)findViewById(R.id.singleImageView);

        mAuth=FirebaseAuth.getInstance();
        deleteButton=(Button)findViewById(R.id.singleDeleteButton);
        deleteButton.setVisibility(View.INVISIBLE);

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            String post_nannyname=(String) dataSnapshot.child("name").getValue();
            String post_desc=(String) dataSnapshot.child("desc").getValue();
            String post_obnumber=(String) dataSnapshot.child("obNumber").getValue();
            String post_image=(String) dataSnapshot.child("image").getValue();
            String post_uid=(String) dataSnapshot.child("uid").getValue();

            singlePostNannyName.setText(post_nannyname);
            singlePostObNumber.setText(post_obnumber);
            singlePostDesc.setText(post_desc);
            Picasso.with(SingleNannyActivity.this).load(post_image).into(singlePostImage);

                if(mAuth.getCurrentUser().getUid().equals(post_uid)){
                    deleteButton.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    public void deleteButtonClicked(View view){
        mDatabase.child(post_key).removeValue();

        Intent caseIntent=new Intent(SingleNannyActivity.this,cases.class);
        startActivity(caseIntent);
    }
}
