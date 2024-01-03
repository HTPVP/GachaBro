package com.example.gachabro;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.TextView;

import com.example.gachabro.Adapters.Char_active_adapter;
import com.example.gachabro.model.Character_items;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Character extends AppCompatActivity {

    TextView usernameTextview;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    NavigationView navigationView;
    View headerview;
    private final Handler handler = new Handler();
    private Dialog progressDialog;

    FirebaseFirestore db;

    List<Character_items> items;

    RecyclerView recyclerView;

    Char_active_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);


        recyclerView = findViewById(R.id.active_char_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spanCount = getResources().getInteger(R.integer.numColumns);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        int finalItemWidth = (screenWidth - (spanCount * spacing)) / spanCount;

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacing(spanCount, spacing, true));


        db = FirebaseFirestore.getInstance();

        items = new ArrayList<>();

        adapter = new Char_active_adapter(Character.this, items);

        recyclerView.setAdapter(adapter);

        EventChangeListener();


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
                        intent = new Intent(Character.this, Profile.class);
                        break;
                    case R.id.Settings:
                        intent = new Intent(Character.this, Settings.class);
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
                        intent = new Intent(Character.this, Home.class);
                        break;
                    case R.id.calculator:
                        intent = new Intent(Character.this, Calculator.class);
                        break;
                    case R.id.status:
                        intent = new Intent(Character.this, Status.class);
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

    private void EventChangeListener(){
        File imgDirectory = new File(getFilesDir(), "character_images");
        File prefixDirectory = new File(getFilesDir(), "Character_Prefixes");

        if (imgDirectory.exists() && prefixDirectory.exists()) {
            File[] imgFiles = imgDirectory.listFiles();
            File[] prefixFiles = prefixDirectory.listFiles();

            // Check if the arrays are not null before sorting
            if (imgFiles != null) {
                Arrays.sort(imgFiles);
            }
            if (prefixFiles != null) {
                Arrays.sort(prefixFiles);
            }

            if (imgFiles != null && prefixFiles != null) {
                for (int i = 0; i < imgFiles.length; i++) {
                    if (imgFiles[i].isFile() && imgFiles[i].getName().endsWith(".png")) {
                        try {
                            byte[] bytes = new byte[(int) prefixFiles[i].length()];
                            FileInputStream fis = new FileInputStream(prefixFiles[i]);
                            fis.read(bytes);
                            fis.close();
                            String prefix = new String(bytes);

                            // Use the prefix
                            Character_items item = new Character_items(imgFiles[i].getName(), prefix);
                            item.setImagePath(imgFiles[i].getName());
                            items.add(item);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        adapter.notifyDataSetChanged();
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