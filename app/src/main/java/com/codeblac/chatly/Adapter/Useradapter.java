package com.codeblac.chatly.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codeblac.chatly.MessageActivity;
import com.codeblac.chatly.R;
import com.codeblac.chatly.UserInfo;
import com.google.firebase.platforminfo.UserAgentPublisher;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Useradapter extends RecyclerView.Adapter<Useradapter.ViewHolder> {

    private Context mc;
    private List<UserInfo> mu;

    public Useradapter(Context mc,List<UserInfo> mu){
        this.mc=mc;
        this.mu=mu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mc).inflate(R.layout.user_item,parent,false);
        return new Useradapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final UserInfo user=mu.get(position);
        if(user==null){
            holder.username.setText("dulle");
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        holder.username.setText(user.getFull_Name());
        if(String.valueOf(user.getImageUrl()).equals("default")){
            holder.profile_image.setImageResource(R.drawable.contact);
        }else{
            Glide.with(mc).load(user.getImageUrl()).into(holder.profile_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mc, MessageActivity.class);
                i.putExtra("userid",user.getUid());
                mc.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mu.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;

        public ViewHolder(View iv){
            super(iv);

            username=iv.findViewById(R.id.dis_username);
            profile_image=iv.findViewById(R.id.dis_profile_pic);


        }
    }

}
