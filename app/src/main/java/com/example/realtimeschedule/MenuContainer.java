package com.example.realtimeschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
//import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MenuContainer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

   DrawerLayout drawerLayout;



    @Override
    public void setContentView(View view) {

        drawerLayout= (DrawerLayout) getLayoutInflater().inflate(R.layout.menu_container, null);
        FrameLayout container= drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar= drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView= drawerLayout.findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.menu_drawer_open,R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewUsers:
                //Toast.makeText(MenuContainer.this, "", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),
                        UsersActivity.class));
                return true;

            case R.id.bookings:
                //Toast.makeText(MenuContainer.this, "", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),
                        BookingsActivity.class));
                return true;

            case R.id.schedule:
                //Toast.makeText(MenuContainer.this, "", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),
                        Schedule.class));
                return true;
        }
        return true;
    }
    protected void allocattedActivityTitle(String titleString){
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(titleString);
        }
    }
}