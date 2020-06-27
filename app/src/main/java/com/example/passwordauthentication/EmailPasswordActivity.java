package com.example.passwordauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    public EditText emailField;
    public EditText pwField;
    public Button register;
    public Button signIn;
    public Button signOut;
    public TextView emailPrompt;
    public TextView pwPrompt;

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
        register = findViewById(R.id.button_register);
        signIn = findViewById(R.id.button_sign_in);
        signOut = findViewById(R.id.button_sign_out);
        emailField = findViewById(R.id.editText_email);
        pwField = findViewById(R.id.editText_pw);
        emailPrompt = findViewById(R.id.textView);
        pwPrompt = findViewById(R.id.textView2);
        setStatus();
        updateUI();
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
                            // set user
                            user = mAuth.getCurrentUser();
                            // alert
                            Toast.makeText(EmailPasswordActivity.this, "Create user successful.", Toast.LENGTH_SHORT).show();
                            // update view
                            setStatus();
                            updateUI();
                            refresh();
                        }
                        else {
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
                            // set user
                            user = mAuth.getCurrentUser();
                            // alert
                            Toast.makeText(EmailPasswordActivity.this, "Authentication succeeded.", Toast.LENGTH_SHORT).show();
                            // update view
                            setStatus();
                            updateUI();
                            refresh();
                        }
                        else {
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
        updateUI();
        refresh();
    }

    //@Override
    public void onClick(View v) {
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

    public void updateUI() {
        if(user == null) {  // show sign-in and register components
            emailPrompt.setVisibility(View.VISIBLE);
            pwPrompt.setVisibility(View.VISIBLE);
            emailField.setVisibility(View.VISIBLE);
            pwField.setVisibility(View.VISIBLE);
            register.setVisibility(View.VISIBLE);
            signIn.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.GONE);
        }
        else {  // hide those components
            emailPrompt.setVisibility(View.GONE);
            pwPrompt.setVisibility(View.GONE);
            emailField.setVisibility(View.GONE);
            pwField.setVisibility(View.GONE);
            register.setVisibility(View.GONE);
            signIn.setVisibility(View.GONE);
            signOut.setVisibility(View.VISIBLE);
        }
    }

}
