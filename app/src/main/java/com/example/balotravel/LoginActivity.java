package com.example.balotravel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private EditText emailEdit, passwordEdit;
    private Button btnlogin, btnregis;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        requestWindowFeature (Window.FEATURE_NO_TITLE);
        getSupportActionBar ().hide ();
        setContentView (R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance ();

        emailEdit = findViewById (R.id.email);
        passwordEdit = findViewById (R.id.password);
        btnlogin = findViewById (R.id.btnlogin);
        btnregis = findViewById (R.id.btnregis);

        btnlogin.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                login ();
            }
        });
        btnregis.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                register ();
            }
        });
    }

    private void register() {
        Intent i = new Intent (LoginActivity.this,RegisterActivity.class);
        startActivity (i);
    }

    private void login() {
        String email, password;
        email = emailEdit.getText ().toString ();
        password = passwordEdit.getText ().toString ();


        if (TextUtils.isEmpty (email)) {
            emailEdit.setError ("Email is required!");
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdit.setError ("Please provide valid email!");
            emailEdit.requestFocus ();
            return;
        }

        if (TextUtils.isEmpty (password)) {
            passwordEdit.setError ("Password is required!");
            passwordEdit.requestFocus ();
            return;
        }

        if(password.length () < 6) {
            passwordEdit.setError ("Min password length should be 6 characters!");
            emailEdit.requestFocus ();
            return;
        }
        mAuth.signInWithEmailAndPassword (email,password).addOnCompleteListener (new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful ()) {
                    Toast.makeText (getApplicationContext (),"Logged in successfully!",Toast.LENGTH_SHORT).show ();
                    Intent intent = new Intent (LoginActivity.this,MainActivity.class);
                    startActivity (intent);
                } else {
                    Toast.makeText (getApplicationContext (),"Login failed! Try again!",Toast.LENGTH_SHORT).show ();
                }
            }
        });

    }
}

