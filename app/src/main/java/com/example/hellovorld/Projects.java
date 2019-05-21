package com.example.hellovorld;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Projects extends Fragment {
    private String DEFAULT_DESIGNER_ACCESS_CODE = "000000";
    private FirebaseFirestore db;
    private String orgEmail;
    ArrayList<Projectsdata> projectsdata;
    public ProgressDialog mProgressDialog;
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
        FirebaseApp.initializeApp(getContext());
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setUpRecyclerView();
                    }
                }, 2500);


            }
        });


        //  collectionReference = db.collection("users").document(user.getUid()).collection("Projects");


        // ProjectsAdapter projectsAdapter = new ProjectsAdapter(getContext(), list);
        //      recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //     recyclerView.setAdapter(firestoreRecyclerAdapter);


        if (isDesignerCodeChanged() && user != null) {
            // getProjectsList();
            Query query = db.collection("projects")
                    .whereEqualTo("AccessID", AccessID);

            //  Query query = collectionReference.orderBy("Name", Query.Direction.ASCENDING);
            FirestoreRecyclerOptions<Projectsdata> options = new FirestoreRecyclerOptions.Builder<Projectsdata>()
                    .setQuery(query, Projectsdata.class)
                    .build();
            firestoreRecyclerAdapter = new FirestoreRecyclerAdapter(options);
            recyclerView.setAdapter(firestoreRecyclerAdapter);
        }
        //     ProjectsAdapter projectsAdapter = new ProjectsAdapter(getContext(), projectsdata);

        //     recyclerView.setAdapter(projectsAdapter);
        // setUpRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void getProjectsList() {
        db.collection("projects")
                .whereEqualTo("AccessID", AccessID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()) {
                            projects = task.getResult();
                            updateUI(user);
                        } else {
                            Log.d(TAG, "Error getting documents:", task.getException());
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            if (projects != null) {
                StringBuilder stringBuilder = new StringBuilder();
                projectsdata = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : projects) {
                    projectsdata.add(new Projectsdata("" + queryDocumentSnapshot.getId()));
                    stringBuilder.append("" + queryDocumentSnapshot.getId());
                    Log.d(TAG, "" + queryDocumentSnapshot.getId());
                    db.collection("projects").document(queryDocumentSnapshot.getId()).collection("Components").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isComplete()) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot1 : task.getResult()) {
                                    Log.d(TAG, queryDocumentSnapshot1.getId());
                                }
                            }
                        }
                    });
                    stringBuilder.append("\n");
                }
                ProjectsAdapter projectsAdapter = new ProjectsAdapter(getContext(), projectsdata);

                recyclerView.setAdapter(projectsAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        user = mAuth.getCurrentUser();
        Query query = db.collection("projects")
                .whereEqualTo("AccessID", AccessID);

        //  Query query = collectionReference.orderBy("Name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Projectsdata> options = new FirestoreRecyclerOptions.Builder<Projectsdata>()
                .setQuery(query, Projectsdata.class)
                .build();
        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter(options);
        recyclerView.setAdapter(firestoreRecyclerAdapter);
        firestoreRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        Query query = db.collection("projects")
                .whereEqualTo("AccessID", AccessID);

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

        //   setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        if (isDesignerCodeChanged() && user != null) {
            Query query = db.collection("projects")
                    .whereEqualTo("AccessID", AccessID);

            //  Query query = collectionReference.orderBy("Name", Query.Direction.ASCENDING);
            FirestoreRecyclerOptions<Projectsdata> options = new FirestoreRecyclerOptions.Builder<Projectsdata>()
                    .setQuery(query, Projectsdata.class)
                    .build();
            firestoreRecyclerAdapter = new FirestoreRecyclerAdapter(options);
            recyclerView.setAdapter(firestoreRecyclerAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        }
        swipeRefreshLayout.setRefreshing(false);
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


    private void queryProjects() {

    }

}
