package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mloginemail, mloginpassword;
    private RelativeLayout mlogin,mgotosignup;
    private TextView mgotoforgotpassword;

    private FirebaseAuth firebaseAuth;

    ProgressBar mprogressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mprogressbar = findViewById(R.id.processbarofmainactivity);

        mloginemail = findViewById(R.id.loginemail);
        mloginpassword = findViewById(R.id.loginpassword);
        mgotosignup = findViewById(R.id.gotosignup);
        mlogin = findViewById(R.id.login);
        mgotoforgotpassword = findViewById(R.id.gotoforgotpassword);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,notesActivity.class));
        }

        mgotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,signup.class));
            }
        });

        mgotoforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,forgotpassword.class));
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mloginemail.getText().toString().trim();
                String password = mloginpassword.getText().toString().trim();

                if(mail.isEmpty()||password.isEmpty()){
                    Toast.makeText(MainActivity.this, "All Field are required", Toast.LENGTH_SHORT).show();
                }else{

                    mprogressbar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                checkMailVertification();
                            }else{
                                Toast.makeText(MainActivity.this, "Account Doesn't Exist", Toast.LENGTH_SHORT).show();
                                mprogressbar.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkMailVertification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser.isEmailVerified()==true){
            Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,notesActivity.class));
        }else{
            mprogressbar.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Verify your email first", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}