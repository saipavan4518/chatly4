package com.codeblac.chatly.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codeblac.chatly.Chat;
import com.codeblac.chatly.MessageActivity;
import com.codeblac.chatly.R;
import com.codeblac.chatly.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.platforminfo.UserAgentPublisher;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Useradapter extends RecyclerView.Adapter<Useradapter.ViewHolder> {

    private Context mc;
    private List<UserInfo> mu;
    private boolean ischat;
     String lastmes;

    public Useradapter(Context mc,List<UserInfo> mu,boolean ischat){
        this.mc=mc;
        this.mu=mu;
        this.ischat=ischat;
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

        if(ischat){
            lastMessage(user.getUid(),holder.last_msg);
        }else{
            holder.last_msg.setVisibility(View.GONE);
        }

        if(ischat){
            if(user.getStatus().equals("online")){
                holder.image_on.setVisibility(View.VISIBLE);
                holder.image_off.setVisibility(View.GONE);
            }else{
                holder.image_off.setVisibility(View.VISIBLE);
                holder.image_on.setVisibility(View.GONE);
            }
        }else{
            holder.image_on.setVisibility(View.GONE);
            holder.image_off.setVisibility(View.GONE);
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
        private ImageView image_on,image_off;
        private TextView last_msg;


        public ViewHolder(View iv){
            super(iv);

            username=iv.findViewById(R.id.dis_username);
            profile_image=iv.findViewById(R.id.dis_profile_pic);
            image_on=iv.findViewById(R.id.image_on);
            image_off=iv.findViewById(R.id.image_off);
            last_msg=iv.findViewById(R.id.last_msg);
        }
    }

    private void lastMessage(final String userid, final TextView last_msg){
        lastmes = "defualt";
        final FirebaseUser fbu = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("Chats");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Chat c = ds.getValue(Chat.class);
                    if(c.getReceiver().equals(fbu.getEmail()) && c.getSender().equals(userid) ||
                        c.getReceiver().equals(userid) && c.getSender().equals(fbu.getEmail())){
                        lastmes = c.getMessage();
                    }
                }

                switch(lastmes){
                    case "default":
                        last_msg.setText("");
                        break;
                    default:
                        last_msg.setText(lastmes);
                        break;
                }
                lastmes = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
