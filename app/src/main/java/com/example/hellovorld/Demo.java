package com.example.hellovorld;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Demo extends AppCompatActivity {
    private String DEFAULT_DESIGNER_ACCESS_CODE = "000000";
    private FirebaseFirestore db;
    private String orgEmail;

    String AccessID = null;
    private CollectionReference collectionReference;
    private RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseUser user;

    boolean isChanged = false;
    private static final String TAG = "EmailPassword";
    SwipeRefreshLayout swipeRefreshLayout;
    // private List<Projectsdata> list;
    QuerySnapshot projects = null;
    private FirestoreRecyclerAdapter firestoreRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        orgEmail = user.getEmail();
        db.collection("users").whereEqualTo("OrgEmail", orgEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
            AccessID=task.getResult().getDocuments().get(0).get("AccessID").toString();
                Toast.makeText(getApplicationContext(),AccessID,Toast.LENGTH_LONG).show();
            }
        });
    }
}
