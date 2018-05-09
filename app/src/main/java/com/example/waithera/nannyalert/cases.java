package com.example.waithera.nannyalert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class cases extends AppCompatActivity {
    private RecyclerView mNannyList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mNannies;

    //implementing search
    EditText search_edit_text;
   // RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<String> nannyNameList;
    ArrayList<String> nannyObList;
    ArrayList<String> nannyDescList;
    ArrayList<String> nannyImageList;
    ArrayList<String> usersList;
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



//original code
        mNannyList=(RecyclerView)findViewById(R.id.nanny_list);
        mNannyList.setHasFixedSize(true);
        mNannyList.setLayoutManager(new LinearLayoutManager(this));
   mDatabase= FirebaseDatabase.getInstance().getReference().child("NannyAlert");
    mAuth=FirebaseAuth.getInstance();
mAuthListener=new FirebaseAuth.AuthStateListener() {
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
  if(firebaseAuth.getCurrentUser()==null){
      Intent registerIntent= new Intent(cases.this,signUp.class);
  registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
  startActivity(registerIntent);
  }
    }
};

        //implementing search
        search_edit_text=(EditText) findViewById(R.id.searchNanny);
        //recyclerView=(RecyclerView)findViewById(R.id.nanny_list);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        nannyNameList=new ArrayList<>();
        nannyObList=new ArrayList<>();
        nannyDescList=new ArrayList<>();
        nannyImageList=new ArrayList<>();
        usersList=new ArrayList<>();

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    setAdapter(s.toString());
                }else{
                    nannyNameList.clear();
                    nannyImageList.clear();
                    nannyDescList.clear();
                    nannyObList.clear();
                    usersList.clear();
                    mNannyList.removeAllViews();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Nanny,NannyViewHolder> FBRA=new FirebaseRecyclerAdapter<Nanny, NannyViewHolder>(
                Nanny.class,
                R.layout.nanny_row,
                NannyViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(NannyViewHolder viewHolder, Nanny model, int position) {

              final  String post_key=getRef(position).getKey().toString();

             viewHolder.setName(model.getName());
                viewHolder.setDesc(model.getDesc());
              viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setObNumber(model.getObNumber());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleNannyActivity= new Intent(cases.this,SingleNannyActivity.class);
                        singleNannyActivity.putExtra("Postid",post_key);
                        startActivity(singleNannyActivity);
                    }
                });
            }
        };
        mNannyList.setAdapter(FBRA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
     getMenuInflater().inflate(R.menu.menu_cases,menu);
     return true;
    }
    public static class NannyViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public NannyViewHolder(View itemView){
            super(itemView);
             mView=itemView;
        }
        public void setName(String name){
            TextView post_name=(TextView)mView.findViewById(R.id.nanny_name);
        post_name.setText(name);
        }

        public void setDesc(String desc){
            TextView post_desc=(TextView)mView.findViewById(R.id.nanny_description);
            post_desc.setText(desc);
        }
       public void setImage(Context ctx, String image){
            ImageView post_image=(ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
        public void setUsername(String userName){
TextView postUserName=(TextView)mView.findViewById(R.id.textUsername);
postUserName.setText(userName);
        }
        public void setObNumber(String obNumber){
            TextView post_obNumber=(TextView)itemView.findViewById(R.id.nanny_obnumber);
            post_obNumber.setText(obNumber);
        }
    }
@Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();

        if(id==R.id.action_settings){
            return true;
        }
        if(id==R.id.addCase){
Intent intent=new Intent(this,PostActivity.class);
startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
}

//search
    private void setAdapter(final String searchedString){


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nannyNameList.clear();
                nannyImageList.clear();
                nannyDescList.clear();
                nannyObList.clear();
                usersList.clear();
                mNannyList.removeAllViews();

                int counter=0;

         for(DataSnapshot snapshot:dataSnapshot.getChildren()){
             //String uid=snapshot.getKey();
             String nanny_name=snapshot.child("name").getValue(String.class);
             String nanny_image=snapshot.child("image").getValue(String.class);
             String nanny_desc=snapshot.child("desc").getValue(String.class);
             String nanny_obNumber=snapshot.child("obNumber").getValue(String.class);
             String username=snapshot.child("username").getValue(String.class);

             if(nanny_name.contains(searchedString)){
                 nannyNameList.add(nanny_name);
                 nannyImageList.add(nanny_image);
                 nannyDescList.add(nanny_desc);
                 nannyObList.add(nanny_obNumber);
                 usersList.add(username);

                 counter++;
             }

             if(counter ==15)
                 break;


         }

         searchAdapter=new SearchAdapter(cases.this,nannyNameList,nannyObList,nannyDescList,nannyImageList,usersList);
                mNannyList.setAdapter(searchAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
