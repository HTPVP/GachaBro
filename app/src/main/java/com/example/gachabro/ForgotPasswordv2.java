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

public class ForgotPasswordv2 extends AppCompatActivity {

    Button button;
    FirebaseUser auth;
    FirebaseFirestore fstore;
    EditText pass,ConfPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwordv2);

        auth = FirebaseAuth.getInstance().getCurrentUser();
        fstore = FirebaseFirestore.getInstance();

        button = findViewById(R.id.Change_Password);
        pass = findViewById(R.id.txt_password_change);
        ConfPass = findViewById(R.id.txt_Confirm_password_change);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = pass.getText().toString();
                String newConfPass = ConfPass.getText().toString();

                if(TextUtils.isEmpty(newPass)){
                    Toast.makeText(ForgotPasswordv2.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(newConfPass)){
                    Toast.makeText(ForgotPasswordv2.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(!isValidPassword(newPass)){
                    Toast.makeText(ForgotPasswordv2.this, "Password Must Be atleast 6 Characters long, have Capital, special Characters, numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!isValidPassword(newConfPass)){
                    Toast.makeText(ForgotPasswordv2.this, "Password Must Be atleast 6 Characters long, have Capital, special Characters, numbers", Toast.LENGTH_LONG).show();
                    return;
                }

                //Check password with confirm password is the same
                if(!newPass.equals(newConfPass)){
                    Toast.makeText(ForgotPasswordv2.this, "Password is not the same", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.updatePassword(newPass)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d("Password", "user password updated");
                                    startActivity(new Intent(ForgotPasswordv2.this, Login.class));
                                    overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
                                }
                            }
                        });
            }
            private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
            private boolean isValidPassword(String password) {
                return password.matches(PASSWORD_PATTERN);
            }
        });

    }
    public void backLogin(View v) {
        Intent BackToLogin = new Intent(this, Login.class);
        startActivity(BackToLogin);
        overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
    }
}