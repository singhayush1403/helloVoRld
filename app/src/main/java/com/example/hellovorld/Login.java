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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText mEmailField;
    private EditText mPasswordField;
    private static final String DEFAULT_DESIGNER_ACCESS_CODE = "000000";
    FirebaseFirestore db;
    FirebaseUser user;
    String id;
    private String orgEmail;
    boolean isChanged = false;
    String AccessID = null;
    QuerySnapshot projects = null;
    public ProgressDialog mProgressDialog;

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {


        super.onStart();
        user = mAuth.getCurrentUser();
        updateUI(user);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            user = mAuth.getCurrentUser();
            orgEmail = user.getEmail();

            db.collection("users")
                    .whereEqualTo("OrgEmail", orgEmail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    startActivity(new Intent(Login.this, Visualiser.class));
                                    finish();
                                } else {
                                    AccessID = (String) task.getResult().getDocuments().get(0).get("AccessID");
                                    isChanged = !task.getResult().getDocuments().get(0).get("AccessID").equals(DEFAULT_DESIGNER_ACCESS_CODE);
                                    id = AccessID;
                                    Intent i = new Intent(Login.this, MainActivity.class);

                                    i.putExtra("ID", id);
                                    startActivity(i);
                                    finish();
                                }
                            } else {
                                Log.d(TAG, "No such users found");

                            }
                        }
                    });


        }
        mEmailField = findViewById(R.id.loginemail);
        mPasswordField = findViewById(R.id.loginpasswd);

        TextView textViewregister = findViewById(R.id.tvregister);
        textViewregister.setOnClickListener(this);
        Button button = findViewById(R.id.login);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        switch (i) {
            case R.id.login:
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case R.id.tvregister:
                startActivity(new Intent(Login.this, Register.class));
                finish();
                break;
        }
    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            // updateUI(user);
                            user = mAuth.getCurrentUser();
                            orgEmail = user.getEmail();

                            db.collection("users")
                                    .whereEqualTo("OrgEmail", orgEmail)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().isEmpty()) {
                                                    startActivity(new Intent(Login.this, Visualiser.class));
                                                    finish();
                                                } else {
                                                    AccessID = (String) task.getResult().getDocuments().get(0).get("AccessID");
                                                    isChanged = !task.getResult().getDocuments().get(0).get("AccessID").equals(DEFAULT_DESIGNER_ACCESS_CODE);
                                                    id = AccessID;
                                                    Intent i = new Intent(Login.this, MainActivity.class);
                                                    i.putExtra("ID", id);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            } else {
                                                Log.d(TAG, "No such users found");

                                            }
                                        }
                                    });

                           /* Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra("USER", user);
                            startActivity(intent);

                            finish(); */
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            // mStatusTextView.setText(R.string.auth_failed);
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    finish();
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





