package com.example.userapplication.student;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.userapplication.R;
import com.example.userapplication.common.NetworkChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class StudentHomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    BottomNavigationView bottomNavigationView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        preferences = PreferenceManager.getDefaultSharedPreferences(StudentHomeActivity.this);
        editor = preferences.edit();

        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.studentHomeBottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeBottomNavigationMenuHome);

        drawerLayout = findViewById(R.id.StudentHomeActivityDrawerLayout);
        navigationView = findViewById(R.id.nv_StudentHomeActivity_navigation_drawer);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.start,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);


    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_navigation_drawer, menu);

        return true;
    }*/

    /*@Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {

         if (item.getItemId() == R.id.homeMenuMyProfile){
             Intent i = new Intent(StudentHomeActivity.this, StudentMyProfileActivity.class);
             startActivity(i);
         } else if (item.getItemId() == R.id.homeMenuLogOut) {
             logout();
         }
         return true;
     }
 */

    private void logout() {
        AlertDialog.Builder ad = new AlertDialog.Builder(StudentHomeActivity.this);
        /* is used to create alert dialog box */
        ad.setTitle("I.E.T.K.");
        ad.setMessage("Are you sure you want to log out ?");
        ad.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        ad.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(StudentHomeActivity.this, StudentLoginActivity.class);
                editor.putBoolean("isLogin",false).commit();
                startActivity(i);
                finish();
            }
        }).create().show();


    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

    HomeFragment homeFragment = new HomeFragment();
    TaskFragment taskFragment = new TaskFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    AddFragment addFragment = new AddFragment();

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.homeBottomNavigationMenuHome){

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHomeStudent,homeFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuTask) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHomeStudent,taskFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuNotification) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHomeStudent,notificationFragment).commit();

        } else if (item.getItemId() == R.id.homeBottomNavigationMenuAdd) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHomeStudent,addFragment).commit();

        }



        // navigation drawer
        if (item.getItemId() == R.id.student_navigation_drawer_MyProfile){
            Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.student_navigation_drawer_settings){
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.student_navigation_drawer_aboutus){
            Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.student_navigation_drawer_contactus){
            Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
        }  else if (item.getItemId() == R.id.student_navigation_drawer_logout){
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        }



        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}