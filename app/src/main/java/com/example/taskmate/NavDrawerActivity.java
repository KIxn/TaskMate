package com.example.taskmate;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class NavDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initializations

        Toast.makeText(this, getIntent().getStringExtra("USER_ID") + " " + getIntent().getStringExtra("PERM"), Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_nav_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawlt);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.navtoolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_draw_open,R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //default to dashboard
        if(savedInstanceState == null){//accounts for run-time changes, such as orientation changes, hence will not default to dashboard under these circumstances
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new DashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.dashboardmnu);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.dashboardmnu:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new DashboardFragment()).commit();
                break;
            case R.id.announcementsmnu:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new AnnouncementsFragment()).commit();
                break;
            case R.id.remindersmnu:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new RemindersFragment()).commit();
                break;
            case R.id.boardroommnu:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new BoardRoomFragment()).commit();
                break;
            case R.id.boredroommnu:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new AdditionalFragment()).commit();
                break;
            case R.id.logoutmnu:
                /////////////////////////////////////////////////
                Toast.makeText(this, "Log-Out!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.aboutmnu:
                //////////////////////////////////////////
                Toast.makeText(this, "About YungK", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deletemnu:
                ///////////////////////////////////
                Toast.makeText(this, "!!!!!!!!!!DELETE USER!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        //close drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}