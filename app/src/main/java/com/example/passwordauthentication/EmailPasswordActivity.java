package com.example.passwordauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailPasswordActivity extends AppCompatActivity {

    private static final String TAG = "EmailPasswordActivity";
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // check if user is signed in
        user = mAuth.getCurrentUser();
        setStatus();

    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            Toast.makeText(EmailPasswordActivity.this, "Create user successful.", Toast.LENGTH_SHORT).show();
                            setStatus();
                            refresh();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Create user failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
                            Toast.makeText(EmailPasswordActivity.this, "Authentication succeeded.", Toast.LENGTH_SHORT).show();
                            setStatus();
                            refresh();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        setStatus();
        refresh();
    }

    //@Override
    public void onClick(View v) {
        EditText emailField = findViewById(R.id.editText_email);
        EditText pwField = findViewById(R.id.editText_pw);
        int i = v.getId();
        if(i == R.id.button_register) {
            if(!emailField.getText().toString().trim().matches("") && !pwField.getText().toString().trim().matches(""))
                createAccount(emailField.getText().toString().trim(), pwField.getText().toString().trim());
        }
        else if(i == R.id.button_sign_in) {
            if(!emailField.getText().toString().trim().matches("") && !pwField.getText().toString().trim().matches(""))
                signIn(emailField.getText().toString().trim(), pwField.getText().toString().trim());
        }
        else if(i == R.id.button_sign_out) {
            signOut();
        }
    }

    public void setStatus() {
        String status = "";
        if(user == null) {
            status = "No one is logged in";
        }
        else {
            status = user.getEmail() + " is logged in";
        }
        TextView statusView = findViewById(R.id.status);
        statusView.setText(status);
    }

    public void refresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}
