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
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class UsersFragment extends Fragment {

    private RecyclerView rv;

    private Useradapter ua;
    private List<UserInfo> mu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_users,container,false);

        rv=v.findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        mu=new ArrayList<>();

        readUsers();
        return v;
    }
    private void readUsers(){
        final FirebaseUser fbu = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dr= FirebaseDatabase.getInstance().getReference().child("users");
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mu.clear();
                for(DataSnapshot sp:dataSnapshot.getChildren()){

                        UserInfo ui=sp.getValue(UserInfo.class);
                        if(!(ui.getUid().equals(fbu.getUid())))
                            mu.add(ui);


                }
                ua =new Useradapter(getContext(),mu);
                rv.setAdapter(ua);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
