package com.example.gachabro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gachabro.Adapters.BannerPager_adapter;
import com.example.gachabro.Adapters.search_bar_adapter;
import com.example.gachabro.fragment.wish_fragment;
import com.example.gachabro.imageLoader.CharImageLoader;
import com.example.gachabro.imageLoader.MaterialLoader;
import com.example.gachabro.imageLoader.WeaponImageLoader;
import com.example.gachabro.model.Character_items;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore fstore;
    Button button;
    TextView text, usernameTextview;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    NavigationView navigationView;
    RecyclerView search_recycle;
    View headerview;
    private List<Character_items> items;
    private FragmentManager fragmentManager;
    private Fragment wishFragment = new wish_fragment();
    String username;
    Context context;
    SearchView searchView,search;
    Character chars;
    ViewPager viewPager;
    search_bar_adapter adapter_search;
    CharImageLoader charImageLoader = new CharImageLoader(this);
    WeaponImageLoader weaponImageLoader = new WeaponImageLoader(this);
    MaterialLoader materialLoader = new MaterialLoader(this);

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Dialog progressDialog;
    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        viewPager = findViewById(R.id.pic_slider);
        fragmentManager = getSupportFragmentManager();
        search_recycle = findViewById(R.id.search_bar_recycle);
        searchView = findViewById(R.id.Search_bar);


        //Image loader
        charImageLoader.listFiles();
        weaponImageLoader.listFiles();
        StorageReference listRef = storageRef.child("/dataset/api-mistress/assets/images/materials");
        materialLoader.listAndDownloadFilesRecursively(listRef);
//        materialLoader.listFiles();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();

        //Check User
        boolean checkboxState = getIntent().getBooleanExtra("CHECKBOX_STATE", true);
        Intent LoginPage = new Intent(getApplicationContext(), Login.class);

        if (checkboxState) {
            if (user == null) {
                startActivity(LoginPage);
            } else {
                username = getIntent().getStringExtra("User_ID_Key");
                if (username != null && !username.isEmpty()) {
                    usernameTextview.setText(username);
                }
            }
        } else {
            username = getIntent().getStringExtra("User_ID_Key");
            if (username != null && !username.isEmpty()) {
                usernameTextview.setText(username);
            }
            startActivity(LoginPage);
        }

        fragmentManager.beginTransaction()
                        .add(R.id.frame_wish, wishFragment)
                        .commit();

        //Search bar
        searchView.setActivated(true);
        searchView.setQueryHint("Type your keyword here");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        items = new ArrayList<>();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search operation

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Update search results as the user types
                filterList(newText);
                return false;
            }
        });

        //RecycleView for searchbar
        search_recycle.setLayoutManager(new LinearLayoutManager(this));
        search_recycle.setHasFixedSize(true);

        adapter_search = new search_bar_adapter(context,items);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Intent intent;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.MyProfile:
                        intent = new Intent(Home.this, Profile.class);
                        break;
                    case R.id.Logout:
                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(getApplicationContext(), Login.class);
                        break;
                    default:
                        return false;
                }
                drawerLayout.closeDrawer(GravityCompat.END);
                showProgressDialog();
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
//                    case R.id.calculator:
//                        intent = new Intent(Home.this, Calculator.class);
//                        break;
                    case R.id.Character:
                        intent = new Intent(Home.this, Character.class);
                        break;
                    case R.id.status:
                        intent = new Intent(Home.this, Status.class);
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
        //ViewPager
        BannerPager_adapter adapter = new BannerPager_adapter(this, new int[]{R.drawable.furina_banner, R.drawable.baizu_banner}, viewPager, handler);
        viewPager.setAdapter(adapter);
        adapter.startAutoScroll();

    }
    public void setFilterList(List<Character_items> filterList){
        this.items = filterList;
        adapter_search.notifyDataSetChanged();
    }
    private void filterList(String newText) {
        List<Character_items> filterList = new ArrayList<>();
        for (Character_items item : items){
            if(item.getPrefix().toLowerCase().contains(newText.toLowerCase())){
                filterList.add(item);
            }
        }
        if(filterList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();
        }
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

        Window window = progressDialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }

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
//    private void readFilesAndStoreInList() {
//        File directory = new File(getFilesDir(), "Character_Prefixes");
//        List<String> data = new ArrayList<>();
//
//        if (directory.exists()) {
//            File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
//            if (files != null) {
//                for (File file : files) {
//                    String fileNameWithoutExtension = file.getName().substring(0, file.getName().lastIndexOf('.'));
//                    data.add(fileNameWithoutExtension);
//                }
//            }
//        }
//
//        // Now, 'data' contains all lines from all .txt files in the directory
//    }
    private void initializeAdapter() {
        adapter_search = new search_bar_adapter(context, items);
        search_recycle.setAdapter(adapter_search);
    }
}

