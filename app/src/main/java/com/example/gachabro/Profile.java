package com.example.gachabro;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity{

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore fstore;
    TextView  usernameTextview;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    NavigationView navigationView;
    View headerview;
    String username;
    private final Handler handler = new Handler();
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //FireBase
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        user = auth.getCurrentUser();

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
                        intent = new Intent(Profile.this, Profile.class);
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
                        intent = new Intent(Profile.this, Home.class);
                        break;
                    case R.id.Character:
                        intent = new Intent(Profile.this, Character.class);
                        break;
                    case R.id.status:
                        intent = new Intent(Profile.this, Status.class);
                        break;
//                    case R.id.calculator:
//                        intent = new Intent(Profile.this, Calculator.class);
//                        break;
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
}