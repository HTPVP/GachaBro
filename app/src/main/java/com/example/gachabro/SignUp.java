package com.example.gachabro;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText Editusername, Editemail, Editpassword, EditconfirmPass;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String UserID;
    private final Handler handler = new Handler();
    private Dialog progressDialog;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Editusername = findViewById(R.id.username);
        Editemail = findViewById(R.id.email);
        Editpassword = findViewById(R.id.txt_password);
        EditconfirmPass = findViewById(R.id.txt_Confirm_password);
        progressBar = findViewById(R.id.progress_bar);
        Button Regs = findViewById(R.id.Create_account);


        Regs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String username = Editusername.getText().toString();
                String email = Editemail.getText().toString();
                String password = Editpassword.getText().toString();
                String confirmPass = EditconfirmPass.getText().toString();

                //Check if data is empty
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(SignUp.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUp.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignUp.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(confirmPass)){
                    Toast.makeText(SignUp.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                //Check if data Invalid
                if(!isValidEmail(email)){
                    Toast.makeText(SignUp.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(!isValidPassword(password)){
                    Toast.makeText(SignUp.this, "Password Must Be atleast 6 Characters long, have Capital, special Characters, numbers", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(!isValidPassword(confirmPass)){
                    Toast.makeText(SignUp.this, "Password Must Be atleast 6 Characters long, have Capital, special Characters, numbers", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                //Check password with confirm password is the same
                if(!password.equals(confirmPass)){
                    Toast.makeText(SignUp.this, "Password is not the same", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Account Created.",
                                            Toast.LENGTH_SHORT).show();
                                    UserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                    DocumentReference documentReference = fStore.collection("users").document(UserID);
                                    Map<String,Object> user_temp = new HashMap<>();
                                    user_temp.put("Username", username);
                                    user_temp.put("email", email);
                                    documentReference.set(user_temp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("UserADDED", "onSuccess: user created for " + UserID);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: " + e.toString());
                                        }
                                    });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUp.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                Intent Username = new Intent(SignUp.this, Home.class);
                Username.putExtra("User_ID_key", username);
                startActivity(Username);
                overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }

            private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";

            private boolean isValidPassword(String password) {
                return password.matches(PASSWORD_PATTERN);
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

    private void showProgressDialog() {
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Delay the dismissal of the progress dialog
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, 500); // Delay in milliseconds
    }
}