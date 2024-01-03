package com.example.gachabro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

import com.example.gachabro.model.Wishes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Status extends AppCompatActivity {

    FirebaseAuth auth;
    String user;
    FirebaseFirestore fstore;

    TextView usernameTextview;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    NavigationView navigationView;
    View headerview;
    TextView character_total, character_five, character_four,
            weapon_total, weapon_five, weapon_four,
            standard_total, standard_five, standard_four;

    Button button;

    List<Wishes> wishes;
    private final Handler handler = new Handler();
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        character_total = findViewById(R.id.character_wish);
        character_five = findViewById(R.id.character_five_star);
        character_four = findViewById(R.id.character_four_star);
        weapon_total = findViewById(R.id.weapon_wish);
        weapon_five = findViewById(R.id.weapon_five_star);
        weapon_four = findViewById(R.id.weapon_four_star);
        standard_total = findViewById(R.id.standard_wish);
        standard_five = findViewById(R.id.standard_five_star);
        standard_four = findViewById(R.id.standard_four_star);

        auth = FirebaseAuth.getInstance();
        this.fstore = FirebaseFirestore.getInstance();

        user = auth.getCurrentUser().getUid();

        DocumentReference documentReference = fstore.collection("wishes").document("KlnE6VjNCNVKE3W0TKjl");
        documentReference.addSnapshotListener(this, (EventListener<DocumentSnapshot>) (value, error) -> {
            character_total.setText(value.getString("total_char"));
            character_five.setText(value.getString("five_char"));
            character_four.setText(value.getString("four_char"));
            weapon_total.setText(value.getString("total_weapon"));
            weapon_five.setText(value.getString("five_weapon"));
            weapon_four.setText(value.getString("four_weapon"));
            standard_total.setText(value.getString("total_standard"));
            standard_five.setText(value.getString("five_standard"));
            standard_four.setText(value.getString("four_standard"));
        });


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
                        intent = new Intent(Status.this, Profile.class);
                        break;
                    case R.id.Settings:
                        intent = new Intent(Status.this, Settings.class);
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
                        intent = new Intent(Status.this, Home.class);
                        break;
                    case R.id.Character:
                        intent = new Intent(Status.this, Character.class);
                        break;
                    case R.id.calculator:
                        intent = new Intent(Status.this, Calculator.class);
                        break;
                    default:
                        return false;
                }
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
        });


    }


    public void buttonClickFunction(View v){
        Intent intent = new Intent(getApplicationContext(), UpdateStatus.class);
        startActivity(intent);
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