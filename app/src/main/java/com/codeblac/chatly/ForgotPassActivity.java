package com.codeblac.chatly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPassActivity extends AppCompatActivity {

    EditText send_email;
    Button btn_reset;

    FirebaseAuth fbu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        Toolbar t=findViewById(R.id.toolbar);
        setSupportActionBar(t);
        getSupportActionBar().setTitle("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        send_email = findViewById(R.id.send_email);
        btn_reset = findViewById(R.id.btn_reset);

        fbu = FirebaseAuth.getInstance();
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = send_email.getText().toString();

                if(email.equals("")){
                    Toast.makeText(ForgotPassActivity.this,"Email is Required",Toast.LENGTH_SHORT).show();
                }else{
                    fbu.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPassActivity.this,"please check your mail",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPassActivity.this,LoginActivity.class));
                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(ForgotPassActivity.this,error,Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }
}
