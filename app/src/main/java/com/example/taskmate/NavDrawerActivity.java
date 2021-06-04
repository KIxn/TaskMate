package com.example.taskmate;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
    private NavigationView navigationView;
    public static String USER_ID; public static String PERM;
    public static FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initializations
        USER_ID = getIntent().getStringExtra("USER_ID");
        PERM = getIntent().getStringExtra("PERM");

        setContentView(R.layout.activity_nav_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawlt);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

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

        //TODO evaluate if Fragments are manages correctly
        //ALTHOUGH ALL SEEMS TO BE FUNCTIONING WELL
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (item.getItemId()){
            case R.id.dashboardmnu:
                //TODO DashBoard
                DashboardFragment dashboardFragment = DashboardFragment.newInstance(USER_ID,PERM);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                fragmentTransaction.addToBackStack("0");
                fragmentTransaction.add(R.id.frag_container,dashboardFragment,"DASHBOARD_FRAGMENT").commit();
                break;
            case R.id.announcementsmnu:
                //TODO Announcements
                AnnouncementsFragment announcementsFragment = AnnouncementsFragment.newInstance(USER_ID, PERM);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                fragmentTransaction.addToBackStack("1");
                fragmentTransaction.add(R.id.frag_container,announcementsFragment,"ANNOUNCEMENTS_FRAGMENT").commit();
                break;
            case R.id.remindersmnu:
                //TODO Reminders
                RemindersFragment remindersFragment = RemindersFragment.newInstance(USER_ID, PERM);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                fragmentTransaction.addToBackStack("2");
                fragmentTransaction.add(R.id.frag_container,remindersFragment,"REMINDERS_FRAGMENT").commit();
                break;
            case R.id.boardroommnu:
                //TODO BoardRoom
                BoardRoomFragment boardRoomFragment = BoardRoomFragment.newInstance(USER_ID,PERM);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                fragmentTransaction.addToBackStack("3");
                fragmentTransaction.add(R.id.frag_container,boardRoomFragment,"BOARDROOM_FRAGMENT").commit();
                break;
            case R.id.boredroommnu:
                //TODO Additional Content
                AdditionalFragment additionalFragment = AdditionalFragment.newInstance(USER_ID,PERM);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
                fragmentTransaction.addToBackStack("4");
                fragmentTransaction.add(R.id.frag_container,additionalFragment,"ADDITIONAL_FRAGMENT").commit();
                break;
            case R.id.logoutmnu:
                //TODO Log-Out
                Toast.makeText(this, "Log-Out!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.aboutmnu:
                //TODO About page
                Toast.makeText(this, "About YungK", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deletemnu:
                //TODO Delete User
                Toast.makeText(this, "!!!!!!!!!!DELETE USER!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        //close drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //closes nav pane if opened & keeps selected menu item responsive to navigation changes
    @Override
    public void onBackPressed() {
        //checks stack frame of fragments
        int count = getSupportFragmentManager().getBackStackEntryCount();
        //close menus if open
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            //handle stack
            //////////////////////////////////
            if (count == 0) {
                super.onBackPressed();
                //additional code
            } else {
                getSupportFragmentManager().popBackStack();
                if(count > 1){
                    //used to update menu
                    switch(Integer.parseInt(getSupportFragmentManager().getBackStackEntryAt(count-2).getName())){
                        case 0:
                            navigationView.setCheckedItem(R.id.dashboardmnu);
                            break;
                        case 1:
                            navigationView.setCheckedItem(R.id.announcementsmnu);
                            break;
                        case 2:
                            navigationView.setCheckedItem(R.id.remindersmnu);
                            break;
                        case 3:
                            navigationView.setCheckedItem(R.id.boardroommnu);
                            break;
                        case 4:
                            navigationView.setCheckedItem(R.id.boredroommnu);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + Integer.parseInt(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()));
                    }
                }else{
                    navigationView.setCheckedItem(R.id.dashboardmnu);
                }
            }
        }
    }
}