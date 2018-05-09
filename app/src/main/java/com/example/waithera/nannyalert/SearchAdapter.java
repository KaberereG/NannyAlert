package com.example.waithera.nannyalert;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Waithera on 20/04/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> nannyNameList;
    ArrayList<String> nannyObList;
    ArrayList<String> nannyDescList;
    ArrayList<String> nannyImageList;
    ArrayList<String> usersList;

    class SearchViewHolder extends RecyclerView.ViewHolder{
            ImageView nannyImage;
            TextView username,nannyName,nannyOb,nannyDesc;

        public SearchViewHolder(View itemView){
            super(itemView);

            nannyImage=(ImageView)itemView.findViewById(R.id.post_image);
            nannyName=(TextView)itemView.findViewById(R.id.nanny_name);
            nannyOb=(TextView)itemView.findViewById(R.id.nanny_obnumber);
            nannyDesc=(TextView)itemView.findViewById(R.id.nanny_description);
            username=(TextView)itemView.findViewById(R.id.textUsername);
        }
    }

    public SearchAdapter(Context context, ArrayList<String> nannyNameList, ArrayList<String> nannyObList, ArrayList<String> nannyDescList, ArrayList<String> nannyImageList, ArrayList<String> usersList) {
        this.context = context;
        this.nannyNameList = nannyNameList;
        this.nannyObList = nannyObList;
        this.nannyDescList = nannyDescList;
        this.nannyImageList = nannyImageList;
        this.usersList = usersList;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.nanny_row,parent,false);

        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
holder.username.setText(usersList.get(position));
holder.nannyName.setText(nannyNameList.get(position));
        holder.nannyOb.setText(nannyObList.get(position));
holder.nannyDesc.setText(nannyDescList.get(position));

        Picasso.with(context).load(nannyImageList.get(position)).into(holder.nannyImage);
    }


    @Override
    public int getItemCount() {
        return nannyNameList.size();
    }
}
