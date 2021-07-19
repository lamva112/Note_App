package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createnote extends AppCompatActivity {

    EditText mcreatitleofnote,mcreatecontentofnote;
    FloatingActionButton msavenote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    ProgressBar progressBarofcreatenote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnote);

        progressBarofcreatenote = findViewById(R.id.processbarofcreatenote);
        msavenote = findViewById(R.id.savenote);
        mcreatecontentofnote = findViewById(R.id.createcontentofnote);
        mcreatitleofnote = findViewById(R.id.createtitleofnote);

        Toolbar toolbar = findViewById(R.id.toolbarofcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        msavenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mcreatitleofnote.getText().toString();
                String content = mcreatecontentofnote.getText().toString();

                if(title.isEmpty() || content.isEmpty()){
                    Toast.makeText(createnote.this, "Both Field are Require", Toast.LENGTH_SHORT).show();
                }else{

                    progressBarofcreatenote.setVisibility(View.VISIBLE);

                    DocumentReference documentReference = firebaseFirestore.collection("Notes")
                            .document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String,Object> note = new HashMap<>();
                    note.put("title",title);
                    note.put("content",content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(createnote.this, "Note Created Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(createnote.this,notesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(createnote.this, "Failed To Create Note", Toast.LENGTH_SHORT).show();
                            progressBarofcreatenote.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}