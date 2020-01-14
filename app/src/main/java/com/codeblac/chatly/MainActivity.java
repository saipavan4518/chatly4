package com.codeblac.chatly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codeblac.chatly.Fragments.ProfileFragment;
import com.codeblac.chatly.Fragments.UsersFragment;
import com.codeblac.chatly.Fragments.chatsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mauth;
    private FirebaseUser muser;
    private DatabaseReference mDatabase;

    CircleImageView profilepic;
    TextView username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth = FirebaseAuth.getInstance();
        muser = mauth.getCurrentUser();

        Toolbar t=findViewById(R.id.toolbar);
        setSupportActionBar(t);
        getSupportActionBar().setTitle("");

        profilepic=findViewById(R.id.profile_pic);
        username=findViewById(R.id.username);


        if(muser!= null){
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
            Query query = mDatabase.orderByChild("uid").equalTo(muser.getEmail());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        UserInfo userInfo = null;
                        for(DataSnapshot childDSS: dataSnapshot.getChildren()){
                            userInfo = childDSS.getValue(UserInfo.class);
                        }
                        username.setText(userInfo.getFull_Name());
                        //if(userInfo.getImageUrl().equals("default")){
                           profilepic.setImageResource(R.drawable.contact);
                        //}else{
                            //Glide.with(MainActivity.this).load(userInfo.getImageUrl()).into(profilepic);
                        //}

                        Toast.makeText(getApplicationContext(),userInfo.getFull_Name()+" "+ userInfo.getUid(),Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        TabLayout tl=findViewById(R.id.tab_layout);
        ViewPager vp=findViewById(R.id.view_pager);

        ViewPagerAdapter vpa=new ViewPagerAdapter(getSupportFragmentManager());

        vpa.addFragment(new chatsFragment(),"Chats");
        vpa.addFragment(new UsersFragment(),"Users");
        vpa.addFragment(new ProfileFragment(),"Profile");

        vp.setAdapter(vpa);

        tl.setupWithViewPager(vp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                return true;
        }
        return false;
    }

    protected void onStart(){
        super.onStart();
        if(muser == null){
            Intent login = new Intent(MainActivity.this,LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            finish();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment f,String title){
            fragments.add(f);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
