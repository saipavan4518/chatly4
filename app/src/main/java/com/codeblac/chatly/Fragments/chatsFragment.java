package com.codeblac.chatly.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeblac.chatly.Adapter.Useradapter;
import com.codeblac.chatly.Chat;
import com.codeblac.chatly.R;
import com.codeblac.chatly.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class chatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private Useradapter useradapter;
    private List<UserInfo> mu,lu;
    private Iterator<UserInfo> itr;

    FirebaseUser fuser;
    DatabaseReference dbr;

    private List<String> userlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chats,container,false);

        recyclerView= v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager((getContext())));

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG,"USER--!--"+fuser.getUid());

        userlist = new ArrayList<>();

        dbr = FirebaseDatabase.getInstance().getReference().child("Chats");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Chat c = ds.getValue(Chat.class);
                    if(c.getSender().equals(fuser.getEmail())){
                        Log.d(TAG,"UserLIst:Reciever:"+c.getReceiver());
                        userlist.add(c.getReceiver());
                    }
                    if(c.getReceiver().equals(fuser.getEmail())){
                        Log.d(TAG,"UserLIst:sender:"+c.getSender());
                        userlist.add(c.getSender());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       return v;
    }
    private void readChats(){
        mu =new ArrayList<>();
        lu =new ArrayList<>();
        itr=mu.iterator();
        dbr = FirebaseDatabase.getInstance().getReference().child("users");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    UserInfo ui=ds.getValue(UserInfo.class);

                    for(String id:userlist){
                        if(String.valueOf(ui.getUid()).equals(id)){
                            if(mu.size()!=0){
                                while(itr.hasNext()){
                                    UserInfo ufo=itr.next();
                                    if(!ui.getUid().equals(ufo.getUid())){
                                        Log.d(TAG,"User:"+ui.getUid());
                                        if(!lu.contains(ui))
                                            lu.add(ui);
                                    }
                                }
                            }else{
                                Log.d(TAG,"UseR:"+ui.getUid());
                                if(!lu.contains(ui))
                                    lu.add(ui);
                            }
                        }
                    }
                }
                useradapter = new Useradapter(getContext(),lu);
                recyclerView.setAdapter(useradapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
