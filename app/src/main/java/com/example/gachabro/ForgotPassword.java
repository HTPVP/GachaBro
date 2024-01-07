package com.example.gachabro;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForgotPassword extends AppCompatActivity {

    Button find;
    EditText email;
    FirebaseUser user;
    FirebaseFirestore fstore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.Findemail);
        find = findViewById(R.id.FindAccount);
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String find_email = email.getText().toString();
                if(TextUtils.isEmpty(find_email)){
                    Toast.makeText(ForgotPassword.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isValidEmail(find_email)){
                    Toast.makeText(ForgotPassword.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(find_email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.d("Emailerror", "Email Sent");
                                    Toast.makeText(ForgotPassword.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ForgotPassword.this, ForgotPasswordv2.class));
                                    overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("EmailError", "onFailure: " + e.toString());
                            }
                        });

            }
            private boolean isValidEmail(String email) {
                return Patterns.EMAIL_ADDRESS.matcher(email).matches();
            }
        });

    }

    public void backLogin(View v) {
        Intent BackToLogin = new Intent(this, Login.class);
        startActivity(BackToLogin);
        overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
    }



}