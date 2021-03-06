package com.example.hellovorld;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    String id;
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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavmenu);
        Fragment fragment;
        bottomNavigationView.getMenu().findItem(R.id.action_profile).setChecked(true);
fragment=new Profile();
getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            orgEmail = user.getEmail();
            db.collection("users").whereEqualTo("OrgEmail", orgEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.getResult().isEmpty()) {
                    startActivity(new Intent(MainActivity.this,Visualiser.class));
                    finish();
                    } else {
                        AccessID = task.getResult().getDocuments().get(0).get("AccessID").toString();
                        
                    }
                }
            });
        } else {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
        id = getIntent().getStringExtra("ID");
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_projects:
                        fragment = new Projects();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", AccessID);
                        fragment.setArguments(bundle);
                        break;
                    case R.id.action_about:
                        fragment = new Aboutus();
                        break;
                    case R.id.action_cart:
                        fragment = new Cart();
                        break;
                    case R.id.action_profile:
                        fragment = new Profile();
                        break;
                    case R.id.action_3d:
                        fragment = new ThreeD();
                        break;
                }


                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                        .commit();
                return true;
            }

        });
    }
}
