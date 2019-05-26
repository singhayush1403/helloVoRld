package com.example.hellovorld;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class VComponents extends Fragment {

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

    QuerySnapshot projects = null;
    private FirestoreRecyclerAdapter firestoreRecyclerAdapter;

    String projectname;

    public VComponents() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectname = getArguments().getString("DocRef");

        }
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (isDesignerCodeChanged() && user != null) {
            // getProjectsList();
            Query query = db.collection("projects").document(projectname).collection("components");

            //  Query query = collectionReference.orderBy("Name", Query.Direction.ASCENDING);
            FirestoreRecyclerOptions<Projectsdata> options = new FirestoreRecyclerOptions.Builder<Projectsdata>()
                    .setQuery(query, Projectsdata.class)
                    .build();
            firestoreRecyclerAdapter = new FirestoreRecyclerAdapter(options);
            recyclerView.setAdapter(firestoreRecyclerAdapter);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vcomponents, container, false);
        recyclerView = view.findViewById(R.id.vcomrv);
        if (isDesignerCodeChanged() && user != null) {
            // getProjectsList();
            Query query = db.collection("Projects").document(projectname).collection("components");

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
        Query query = db.collection("projects").document(projectname).collection("components");
        //  Query query = collectionReference.orderBy("Name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Projectsdata> options = new FirestoreRecyclerOptions.Builder<Projectsdata>()
                .setQuery(query, Projectsdata.class)
                .build();
        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter(options);
        firestoreRecyclerAdapter.setOnItemClickListener(new FirestoreRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                String ref = snapshot.getId();

                Toast.makeText(getContext(), ref, Toast.LENGTH_SHORT).show();
                OpenComponents(ref);
            }
        });
        recyclerView.setAdapter(firestoreRecyclerAdapter);
        firestoreRecyclerAdapter.startListening();
    }
    public boolean isDesignerCodeChanged() {

        orgEmail = user.getEmail();

        db.collection("users_v")
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


    @Override
    public void onStop() {
        super.onStop();
        Query query = db.collection("projects").document(projectname).collection("components");
        FirestoreRecyclerOptions<Projectsdata> options = new FirestoreRecyclerOptions.Builder<Projectsdata>()
                .setQuery(query, Projectsdata.class)
                .build();
        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter(options);
        recyclerView.setAdapter(firestoreRecyclerAdapter);

        firestoreRecyclerAdapter.stopListening();
    }
    private void OpenComponents(String ref) {


        Intent intent = new Intent(getActivity(), UploadImage.class);
        intent.putExtra("DocRef", ref);
        intent.putExtra("Projectname", projectname);
        startActivity(intent);

    }
}
