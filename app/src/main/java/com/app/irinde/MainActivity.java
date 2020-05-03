package com.app.irinde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        setUpDefaultFragment();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.amakuru:
                    selectedFragment = new Amakuru();break;
                case R.id.covid19:
                    selectedFragment = new Covid();break;
                case R.id.isiyose:
                    selectedFragment = new Isiyose();break;
                case R.id.ubutabazi:
                    selectedFragment = new Ubutabazi();break;
            }
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            ft.replace(R.id.frame_container,selectedFragment).commit();
            return true;
        }
    };
    void setUpDefaultFragment(){
        Fragment selectedFragment = new Covid();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.replace(R.id.frame_container,selectedFragment).commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        String msg = "";
        int id = item.getItemId();
        Fragment fragment;
        if (id == R.id.ubutabazi) {
            msg = "Add New Period";

            fragment = new Ubutabazi();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame_container, fragment);
            ft.commit();

        }
        if(id == R.id.contacts){
            startActivity(new Intent(MainActivity.this,Contact.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
