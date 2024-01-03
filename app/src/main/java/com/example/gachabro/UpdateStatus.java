package com.example.gachabro;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateStatus extends AppCompatActivity implements View.OnClickListener{
    TextView text, usernameTextview;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    NavigationView navigationView;
    View headerview;
    TextView edit_character_total, edit_character_five, edit_character_four,
            edit_weapon_total, edit_weapon_five, edit_weapon_four,
            edit_standard_total, edit_standard_five, edit_standard_four;
    FirebaseFirestore fstore;

    Button button;
    private final Handler handler = new Handler();
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_status);
        edit_character_total = findViewById(R.id.edittext_total_char);
        edit_character_five = findViewById(R.id.edittext_five_char);
        edit_character_four = findViewById(R.id.edittext_four_char);
        edit_weapon_total = findViewById(R.id.edittext_total_weapon);
        edit_weapon_five = findViewById(R.id.edittext_five_weapon);
        edit_weapon_four = findViewById(R.id.edittext_four_weapon);
        edit_standard_total = findViewById(R.id.edittext_total_standard);
        edit_standard_five = findViewById(R.id.edittext_five_standard);
        edit_standard_four = findViewById(R.id.edittext_four_standard);

        this.fstore = FirebaseFirestore.getInstance();

        findViewById(R.id.button_update).setOnClickListener(this);


        //Design
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        headerview = navigationView.getHeaderView(0);
        toolbar = findViewById(R.id.TopNav);
        usernameTextview = headerview.findViewById(R.id.Username);
        drawerLayout = findViewById(R.id.drawer_layout);
        Intent LoginPage = new Intent(getApplicationContext(), Login.class);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Intent intent;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.MyProfile:
                        intent = new Intent(UpdateStatus.this, Profile.class);
                        break;
                    case R.id.Settings:
                        intent = new Intent(UpdateStatus.this, Settings.class);
                        break;
                    case R.id.Logout:
                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(getApplicationContext(), Login.class);
                        break;
                    default:
                        return false;
                }
                // Close the drawer after selection
                drawerLayout.closeDrawer(GravityCompat.END);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Intent intent;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        intent = new Intent(UpdateStatus.this, Home.class);
                        break;
                    case R.id.Character:
                        intent = new Intent(UpdateStatus.this, Character.class);
                        break;
                    case R.id.status:
                        intent = new Intent(UpdateStatus.this, Status.class);
                        break;
                    default:
                        return false;
                }
//                showProgressDialog();
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
        });


    }

    public void updateData() {
        String total_char = edit_character_total.getText().toString();
        String five_char = edit_character_five.getText().toString();
        String four_char = edit_character_four.getText().toString();

        String total_weapon = edit_weapon_total.getText().toString();
        String five_weapon = edit_weapon_five.getText().toString();
        String four_weapon = edit_weapon_four.getText().toString();

        String total_standard = edit_standard_total.getText().toString();
        String five_standard = edit_standard_five.getText().toString();
        String four_standard = edit_standard_four.getText().toString();


        fstore.collection("wishes").document("KlnE6VjNCNVKE3W0TKjl")
                .update(
                        "total_char", total_char,
                        "five_char", five_char,
                        "four_char", four_char,
                        "total_weapon", total_weapon,
                        "five_weapon", five_weapon,
                        "four_weapon", four_weapon,
                        "total_standard", total_standard,
                        "five_standard", five_standard,
                        "four_standard", four_standard
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateStatus.this, "Data Updated", Toast.LENGTH_LONG).show();
                    }

                    ;
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.ProfileIcon) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_update:
                updateData();
                break;
        }
    }
}
