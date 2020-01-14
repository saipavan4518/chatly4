package com.codeblac.chatly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mauth;
    private FirebaseUser muser;
    private Button signup,login;
    private TextView u_name,u_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mauth = FirebaseAuth.getInstance();
        muser = mauth.getCurrentUser();

        Toolbar t=findViewById(R.id.toolbar);
        setSupportActionBar(t);
        getSupportActionBar().setTitle("Login");


        u_name = findViewById(R.id.login_username);
        u_pass = findViewById(R.id.login_password);

        signup = findViewById(R.id.register_page);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(register);
            }
        });

        login = findViewById(R.id.login_loginactivity);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = u_name.getText().toString();
                String user_pass = u_pass.getText().toString();
                if(user_name.equals("") || user_pass.equals("")){
                    Toast.makeText(LoginActivity.this,"Enter the Fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    mauth.signInWithEmailAndPassword(user_name, user_pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Sign in Successful", Toast.LENGTH_SHORT).show();
                                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(mainIntent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    protected void onStart(){
        super.onStart();
        if(muser!=null){
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
        }
    }
}
