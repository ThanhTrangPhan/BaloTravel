package com.example.balotravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.example.balotravel.Fragment.BookmarkFragment;
import com.example.balotravel.Fragment.HomepageFragment;
import com.example.balotravel.Fragment.SearchFragment;
import com.example.balotravel.Fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationMenuView;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        bottomNavigationMenuView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationMenuView.setBackground(null);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomepageFragment()).commit();
        bottomNavigationMenuView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomepageFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.nav_add:
                            selectedFragment = null;
                            MainActivity.this.startActivity(new Intent(MainActivity.this, CreateNewJouneyActivity.class));
                            break;
                        case R.id.nav_bookmark:
                            selectedFragment = new BookmarkFragment();
                            break;
                        case R.id.nav_person:
                            selectedFragment = new UserFragment();
                            SharedPreferences.Editor editor = getSharedPreferences("PREPS",MODE_PRIVATE).edit();
//                            editor.putString("profileId", FirebaseAuth.getInstance().getCurrentUser().getUid());
//                            editor.apply();
                            selectedFragment = new UserFragment();
                            break;
                    }
                    if(selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    }
                    return true;
                }
            };
}