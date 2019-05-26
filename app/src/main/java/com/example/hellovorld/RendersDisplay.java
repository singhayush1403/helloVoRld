package com.example.hellovorld;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RendersDisplay extends AppCompatActivity {
    ProgressBar mProgressCircle;
    ImageAdapter mImageAdapter;
    List<Changes> mChanges;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    String Component;
    String projectname;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renders_display);
        recyclerView = findViewById(R.id.renderrv);
        mProgressCircle = findViewById(R.id.progress_circle);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Component = getIntent().getStringExtra("DocRef");
projectname=getIntent().getStringExtra("Projectname");
RendersDisplay.this.setTitle(Component);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChanges = new ArrayList<>();
        db.collection("projects").document(projectname).collection("components").document(Component).collection("Renders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    Changes changes = queryDocumentSnapshot.toObject(Changes.class);
                    mChanges.add(changes);
                }
                mImageAdapter = new ImageAdapter(RendersDisplay.this, mChanges);
                recyclerView.setAdapter(mImageAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

        });


    }

}
