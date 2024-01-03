package com.example.gachabro;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView google_btn;
    Button Login;
    EditText Editusername, Editemail, Editpassword, EditconfirmPass;
    ProgressBar progressbar;
    CheckBox Check;
    FirebaseAuth mAuth;
    private final Handler handler = new Handler();
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        //Normal Login
        mAuth = FirebaseAuth.getInstance();

        Editusername = findViewById(R.id.username);
        Editpassword = findViewById(R.id.password);
        Editemail = findViewById(R.id.username);
        Login = findViewById(R.id.Login);
        progressbar = findViewById(R.id.progress_bar);
        Check = findViewById(R.id.check_remember);

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                String username = Editusername.getText().toString();
                String email = Editemail.getText().toString();
                String password = Editpassword.getText().toString();

                //Check if data is empty
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(Login.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                    return;
                }


                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    Log.d(TAG, "signInWithEmail:success");
                                    boolean isChecked = Check.isChecked();
                                    editor.putBoolean("remember", isChecked);
                                    editor.apply();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    toHomePage();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    progressbar.setVisibility(View.GONE);
                                }
                            }

                            private void updateUI(FirebaseUser user) {
                            }
                        });
            }
        });


        //Google Login
        google_btn = findViewById(R.id.google_btn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        sharedPreferences.getBoolean("remember", false);
        if(currentUser != null){
            showProgressDialog();
            toHomePage();
            overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
        }
    }
    public void signIn(){
        Intent signInGoogle = gsc.getSignInIntent();
        startActivityForResult(signInGoogle, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                task.getResult(ApiException.class);
                toHomePage();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void toHomePage() {
        finish();
        Intent HomePage = new Intent(this, Home.class);
        HomePage.putExtra("checkboxstate", true);
        showProgressDialog();
        startActivity(HomePage);
        overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
    }

    public void submitNewAcc(View v){
        Intent newAcc = new Intent(this, SignUp.class);
        showProgressDialog();
        startActivity(newAcc);
        overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
    }

    public void ForgotPass(View v){
        Intent ForPass = new Intent(this, ForgotPassword.class);
        showProgressDialog();
        startActivity(ForPass);
        overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
    }
    private void showProgressDialog() {
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        finish();

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