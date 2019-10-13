package com.example.clientactivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.view.GravityCompat;
import android.widget.Toast;
import android.support.annotation.NonNull;

public class Main2Activity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        NavigationView navigation_View = findViewById(R.id.nav_view);
        navigation_View.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.title ) {
                    Toast.makeText(Main2Activity.this, "點餐", Toast.LENGTH_SHORT).show();
                    finish();
                    return true;
                }
                else if(id == R.id.menu ){
                    return true;
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }

        });


        }


    }


