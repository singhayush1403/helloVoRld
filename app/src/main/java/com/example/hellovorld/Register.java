package com.example.hellovorld;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private static final String DEFAULT_DESIGNER_ACCESS_CODE = "000000";
    FirebaseFirestore db;
    FirebaseUser user;
    RadioGroup radioGroup;
    private EditText mEmailField;
    private EditText mPasswordField;
    boolean isChanged = false;
    String AccessID = null;
    QuerySnapshot projects = null;
    public ProgressDialog mProgressDialog;

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        TextView alreadyregister = findViewById(R.id.alreadyregister);
        alreadyregister.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mEmailField = findViewById(R.id.emailregister);
        mPasswordField = findViewById(R.id.passwdregister);
        Button register = findViewById(R.id.buttonregister);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                id = radioGroup.getCheckedRadioButtonId();

            }
        });
        register.setOnClickListener(this);

    }

    private void createVisualiserAccount(String email, String password) {
        Log.d(TAG, "createVisualiserAccount: " + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    user = mAuth.getCurrentUser();
                    updateUI(user);
                    createVisualiserAccountInFirestore(user);
                    Intent intent = new Intent(Register.this, Visualiser.class);

                    startActivity(intent);

                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Register.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
                hideProgressDialog();
            }
        });

    }

    private void createVisualiserAccountInFirestore(FirebaseUser user) {
        Map<String, Object> userObject = new HashMap<>();
        userObject.put("OrgEmail", user.getEmail());
        userObject.put("AccessID", DEFAULT_DESIGNER_ACCESS_CODE);

        db.collection("users_v")
                .add(userObject)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Successfully added user");
                        queryProjects();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "create account" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    user = mAuth.getCurrentUser();
                    updateUI(user);
                    createUserAccountInFirestore(user);
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    intent.putExtra("USER", user);
                    startActivity(intent);

                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Register.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
                hideProgressDialog();
            }
        });
    }

    private void createUserAccountInFirestore(FirebaseUser user) {
        Map<String, Object> userObject = new HashMap<>();
        userObject.put("OrgEmail", user.getEmail());
        userObject.put("AccessID", DEFAULT_DESIGNER_ACCESS_CODE);

        db.collection("users")
                .add(userObject)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Successfully added user");
                        queryProjects();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void queryProjects() {
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            if (projects != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (QueryDocumentSnapshot queryDocumentSnapshot : projects) {
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
            }

        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        switch (i) {
            case R.id.buttonregister:
                if (id == R.id.designer) {
                    Toast.makeText(this,"designer pressed",Toast.LENGTH_SHORT).show();
                    createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
                } else if (id == R.id.visualiser) {
                    Toast.makeText(this,"Visualiser pressed",Toast.LENGTH_SHORT).show();
                   createVisualiserAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
                }
                break;
            case R.id.alreadyregister:
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                break;
        }
    }
}
