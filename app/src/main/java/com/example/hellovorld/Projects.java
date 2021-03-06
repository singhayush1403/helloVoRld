package com.example.hellovorld;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;


public class Projects extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        recyclerView = view.findViewById(R.id.projectsrv);


        if (isDesignerCodeChanged() && user != null) {
            // getProjectsList();
            Query query = db.collection("projects")
                    .whereEqualTo("AccessID", AccessID);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.getResult().isEmpty()) {
                        Toast.makeText(getContext(),"NO PROJECTS",Toast.LENGTH_LONG).show();
                    }
                }
            });

            //  Query query = collectionReference.orderBy("Name", Query.Direction.ASCENDING);
            FirestoreRecyclerOptions<Projectsdata> options = new FirestoreRecyclerOptions.Builder<Projectsdata>()
                    .setQuery(query, Projectsdata.class)
                    .build();
            firestoreRecyclerAdapter = new FirestoreRecyclerAdapter(options);
            recyclerView.setAdapter(firestoreRecyclerAdapter);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        user = mAuth.getCurrentUser();
        Query query = db.collection("projects")
                .whereEqualTo("AccessID", AccessID);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()) {
                    Toast.makeText(getContext(),"NO PROJECTS",Toast.LENGTH_LONG).show();
                }
            }
        });

        //  Query query = collectionReference.orderBy("Name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Projectsdata> options = new FirestoreRecyclerOptions.Builder<Projectsdata>()
                .setQuery(query, Projectsdata.class)
                .build();
        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter(options);
        recyclerView.setAdapter(firestoreRecyclerAdapter);
        firestoreRecyclerAdapter.setOnItemClickListener(new FirestoreRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                String ref = snapshot.getId();

                Toast.makeText(getContext(), ref, Toast.LENGTH_SHORT).show();
                OpenComponents(ref);

            }
        });
        firestoreRecyclerAdapter.startListening();
    }

    private void OpenComponents(String ref) {
        Bundle bundle = new Bundle();
        bundle.putString("DocRef", ref);

        Components components = new Components();
        components.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, components).addToBackStack(null).commit();


    }

    @Override
    public void onStop() {
        super.onStop();
        Query query = db.collection("projects")
                .whereEqualTo("AccessID", AccessID);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()) {
                    Toast.makeText(getContext(),"NO PROJECTS",Toast.LENGTH_LONG).show();
                }
            }
        });

        //  Query query = collectionReference.orderBy("Name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Projectsdata> options = new FirestoreRecyclerOptions.Builder<Projectsdata>()
                .setQuery(query, Projectsdata.class)
                .build();
        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter(options);
        recyclerView.setAdapter(firestoreRecyclerAdapter);


        firestoreRecyclerAdapter.stopListening();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            AccessID = getArguments().getString("id");
        }


    }


    public boolean isDesignerCodeChanged() {

        orgEmail = user.getEmail();

        db.collection("users")
                .whereEqualTo("OrgEmail", orgEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            AccessID = (String) task.getResult().getDocuments().get(0).get("AccessID");
                            isChanged = !task.getResult().getDocuments().get(0).get("AccessID").equals(DEFAULT_DESIGNER_ACCESS_CODE);
                        } else {
                            Log.d(TAG, "No such users found");
                        }
                    }
                });
        return isChanged;
    }


}
