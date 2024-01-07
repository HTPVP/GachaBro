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
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gachabro.Adapters.Calcu_adapter;
import com.example.gachabro.model.Character_items;
import com.example.gachabro.model.Weapon_items;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.search.SearchBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Calculator extends AppCompatActivity {

    TextView text, usernameTextview;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    private List<Character_items> items_char;
    private List<Weapon_items> items_weapon;
    SearchView char_search, wpn_search;
    NavigationView navigationView;
    View headerview;
    StorageReference storageRef;
    Calcu_adapter calcu_adapter;
    private final Handler handler = new Handler();
    private Dialog progressDialog;
    Context context;
    Button add_char, add_wpn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        //Design
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        headerview = navigationView.getHeaderView(0);
        toolbar = findViewById(R.id.TopNav);
        usernameTextview = headerview.findViewById(R.id.Username);
        drawerLayout = findViewById(R.id.drawer_layout);
        add_char = findViewById(R.id.add_Char);
        add_wpn = findViewById(R.id.add_weapon);
        char_search = findViewById(R.id.search_bar_calcu_char);
        wpn_search = findViewById(R.id.search_bar_calcu_wpn);

        char_search.setActivated(true);
        char_search.setQueryHint("Type your keyword here");
        char_search.onActionViewExpanded();
        char_search.setIconified(false);
        char_search.clearFocus();

        wpn_search.setActivated(true);
        wpn_search.setQueryHint("Type your keyword here");
        wpn_search.onActionViewExpanded();
        wpn_search.setIconified(false);
        wpn_search.clearFocus();

        items_char = new ArrayList<>();
        items_weapon = new ArrayList<>();


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
                        intent = new Intent(Calculator.this, Profile.class);
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
                        intent = new Intent(Calculator.this, Home.class);
                        break;
                    case R.id.Character:
                        intent = new Intent(Calculator.this, Character.class);
                        break;
                    case R.id.status:
                        intent = new Intent(Calculator.this, Status.class);
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

        //Search Items
        add_char.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                char_search.setVisibility(View.VISIBLE);
                wpn_search.setVisibility(View.GONE);
                char_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filterCharacter(newText);
                        return true;
                    }
                });
            }
        });

        add_wpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                char_search.setVisibility(View.GONE);
                wpn_search.setVisibility(View.VISIBLE);
                wpn_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filterWeapons(newText);
                        return true;
                    }
                });
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
    private void filterCharacter(String newText) {
        List<Character_items> filterList = new ArrayList<>();
        for (Character_items item : items_char){
            if(item.getPrefix().toLowerCase().contains(newText.toLowerCase())){
                filterList.add(item);
            }
        }
        if(filterList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void filterWeapons(String newText) {
        List<Weapon_items> filterList = new ArrayList<>();
        for (Weapon_items item : items_weapon){
            if(item.getPrefix().toLowerCase().contains(newText.toLowerCase())){
                filterList.add(item);
            }
        }
        if(filterList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }


}