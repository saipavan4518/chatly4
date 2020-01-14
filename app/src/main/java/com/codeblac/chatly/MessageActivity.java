package com.codeblac.chatly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codeblac.chatly.Adapter.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    CircleImageView profile_image;
    TextView username;

    FirebaseUser fbu;
    DatabaseReference dbr,fbrr;

    Intent i;

    MessageAdapter madapter;
    List<Chat> mchat;

    RecyclerView recyclerView;

    ImageButton btn_send;
    EditText text_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image=findViewById(R.id.profile_pic);
        username=findViewById(R.id.username);
        btn_send=findViewById(R.id.btn_send);
        text_send=findViewById(R.id.text_send);


        i=getIntent();
        final String userid=i.getStringExtra("userid");
        Log.d(TAG,"This is the reciever:"+userid);

        fbu = FirebaseAuth.getInstance().getCurrentUser();


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fbu.getEmail(),userid,msg);
                }
                text_send.setText("");
            }
        });


        dbr= FirebaseDatabase.getInstance().getReference().child("users");

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    UserInfo ui=ds.getValue(UserInfo.class);
                    if(String.valueOf(ui.getUid()).equals(userid)){
                        username.setText(ui.getFull_Name());
                        profile_image.setImageResource(R.drawable.contact);
                        Log.d(TAG,"This is the recieverMain:"+ui.getUid());
                        Log.d(TAG,"This is the Sender:"+fbu.getEmail());
                        readMessages(fbu.getEmail(),ui.getUid(),ui.getImageUrl());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender,String rec,String message){
        Log.d(TAG,"Sender:"+sender);
        Log.d(TAG,"Receiver:"+rec);

        DatabaseReference dr=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> map=new HashMap<>();
        map.put("sender",sender);
        map.put("receiver",rec);
        map.put("message",message);

        dr.child("Chats").push().setValue(map);

    }

    private void readMessages(final String myid, final String userid, final String imagurl){
        mchat=new ArrayList<>();
        dbr = FirebaseDatabase.getInstance().getReference().child("Chats");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    Chat c = ds.getValue(Chat.class);
                    Log.d(TAG,"RecieverNExt:"+c.getReceiver());
                    if (c.getReceiver().equals(myid) && c.getSender().equals(userid)) {
                        Log.d(TAG, "onDataChange: received message");
                        mchat.add(c);
                    }
                    if (c.getReceiver().equals(userid) && c.getSender().equals(myid)) {
                        Log.d(TAG, "onDataChangE: sent message");
                        mchat.add(c);
                    }

                }
                    madapter =new MessageAdapter(MessageActivity.this,mchat,imagurl);
                    recyclerView.setAdapter(madapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
