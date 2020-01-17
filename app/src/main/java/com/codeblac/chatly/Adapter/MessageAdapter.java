package com.codeblac.chatly.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.codeblac.chatly.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter   extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static final String TAG = "MyActivity";
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private Context mc;
    private List<Chat> mchat;
    private String imageurl;

    FirebaseUser fbu;

    public MessageAdapter(Context mc, List<Chat> mchat,String imageurl){
        this.mc=mc;
        this.mchat=mchat;
        this.imageurl=imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(mc).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(v);
        }else{
            View v = LayoutInflater.from(mc).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat=mchat.get(position);
        holder.show_message.setText(chat.getMessage());
        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.drawable.contact);
        }else{
            Glide.with(mc).load(imageurl).into(holder.profile_image);
        }
        if(position == mchat.size()-1){
            if(chat.isIsseen()){
                holder.text_seen.setText("seen");
            }else{
                holder.text_seen.setText("Delivered");
            }
        }else{
            holder.text_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;

        public TextView text_seen;

        public ViewHolder(View iv){
            super(iv);

            show_message=iv.findViewById(R.id.show_message);
            profile_image=iv.findViewById(R.id.profile_pic);
            text_seen=iv.findViewById(R.id.text_seen);


        }
    }

    @Override
    public int getItemViewType(int position) {
        fbu = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG,"AUTH-sender:"+mchat.get(position).getSender());
        Log.d(TAG,"REAL-sender:"+fbu.getEmail());
        if(mchat.get(position).getSender().equals(fbu.getEmail())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
