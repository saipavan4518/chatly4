package com.codeblac.chatly;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private Button login,sign_up;
    private TextView u_email, u_pass,u_cnfpass,u_name;

    private FirebaseAuth mauth;
    private FirebaseUser muser;
    private DatabaseReference mDatabase;

    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
        setContentView(R.layout.activity_signup);

        mauth = FirebaseAuth.getInstance();
        muser = mauth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        u_email = findViewById(R.id.signup_email);
        u_pass = findViewById(R.id.signup_pass);
        u_cnfpass = findViewById(R.id.signup_cnfpass);
        u_name = findViewById(R.id.signup_name);

        login = findViewById(R.id.login_page);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sendToLogin();
            }
        });

        sign_up = findViewById(R.id.signup_signupactivity);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }

    private void startRegister() {
        final String user_email = u_email.getText().toString();
        String user_pass = u_pass.getText().toString();
        String user_cnfpass = u_cnfpass.getText().toString();
        final String user_name = u_name.getText().toString();

        if(user_email.isEmpty() || user_pass.isEmpty() || user_cnfpass.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(),"Please Fill All Details",Toast.LENGTH_SHORT);
            toast.show();
        }else{
            mauth.createUserWithEmailAndPassword(user_email,user_pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            userInfo = new UserInfo();
                            userInfo.setFull_Name(user_name);
                            userInfo.setBio("Hello");
                            userInfo.setUid(user_email);
                            mDatabase.push().setValue(userInfo);
                            sendToLogin();
                            Toast.makeText(getApplicationContext(), "Successful",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                }
            });
        }

    }

    protected void onStart(){
        super.onStart();
        if(muser!=null){
            Intent mainIntent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(mainIntent);
        }
    }

    protected void sendToLogin(){
        Intent loginintent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(loginintent);
    }
}
