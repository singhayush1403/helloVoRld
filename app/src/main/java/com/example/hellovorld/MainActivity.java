package com.example.hellovorld;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavmenu);
        Fragment fragment;
        fragment=new Projects();
       getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_projects:
                        fragment = new Projects();
                        break;
                    case R.id.action_about:
                        fragment = new Aboutus();
                        break;
                    case R.id.action_cart:
                        fragment = new Cart();
                        break;
                    case R.id.action_profile:
                        fragment = new Profile();
                        break;
                    case R.id.action_3d:
                        fragment = new ThreeD();
                        break;
                }


                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                return true;
            }

        });
    }
}
