package com.example.balotravel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.balotravel.Fragment.HomepageFragment;
import com.example.balotravel.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEdit, passwordEdit;
    private Button btnregis;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        requestWindowFeature (Window.FEATURE_NO_TITLE);
        getSupportActionBar ().hide ();
        setContentView (R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance ();

        emailEdit = findViewById (R.id.email);
        passwordEdit = findViewById (R.id.password);
        btnregis = findViewById (R.id.btnregis);

        btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
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

        mAuth.createUserWithEmailAndPassword (email,password).addOnCompleteListener (new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful ()) {
                    Log.d("UserID",mAuth.getCurrentUser().getUid());
                    User user = new User("",0,"","","https://firebasestorage.googleapis.com/v0/b/balotravel-9a424.appspot.com/o/2wallpaperflare.com_wallpaper.jpg?alt=media&token=8c87014d-ea58-4f36-9c78-713733190b43",mAuth.getCurrentUser().getUid());
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://balotravel-9a424-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users");
                    mDatabase.setValue(mAuth.getCurrentUser().getUid());
                    mDatabase.child(mAuth.getCurrentUser().getUid()).setValue(user);
                    Toast.makeText (getApplicationContext (),"User has been registered successfully!",Toast.LENGTH_SHORT).show ();
                    Intent intent = new Intent (RegisterActivity.this,EditProfileActivity.class);
                    intent.putExtra("currentUser",user);
                    startActivity (intent);

                } else {
                    Toast.makeText (getApplicationContext (),"Failed to register! Try again !",Toast.LENGTH_SHORT).show ();
                }
            }
        });
    }
}

