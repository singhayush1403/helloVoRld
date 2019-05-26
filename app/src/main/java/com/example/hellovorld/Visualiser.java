package com.example.hellovorld;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Visualiser extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiser);
        FrameLayout frameLayout = findViewById(R.id.containerv);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottommenu);
        bottomNavigationView.getMenu().findItem(R.id.action_projects).setChecked(true);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        orgEmail = user.getEmail();
        db.collection("users_v").whereEqualTo("OrgEmail", orgEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                AccessID=task.getResult().getDocuments().get(0).get("AccessID").toString();
                Toast.makeText(getApplicationContext(),AccessID,Toast.LENGTH_LONG).show();
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_projects:
                        fragment = new VProjects();

                        Bundle bundle = new Bundle();
                        bundle.putString("id",AccessID);
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


                getSupportFragmentManager().beginTransaction().replace(R.id.containerv, fragment)
                        .commit();
                return true;
            }

        });
    }
}
