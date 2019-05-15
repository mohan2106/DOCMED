package com.lenovo.doc;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class WelcomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
                     {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private String username;
    private String email;
    private TextView curuser_name;
    private LinearLayout book_appoint,buy_med;
    private TextView curuser_email;
    private CircleImageView curuser_image;
    private ActionBarDrawerToggle toggle;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        firebaseAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        book_appoint=(LinearLayout)findViewById(R.id.book_appointment);
        buy_med=(LinearLayout)findViewById(R.id.buy_medicine);
        /*book_appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,BookAppointment.class));
            }
        });*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("DocPlus");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        View navheaderView = navigationView.getHeaderView(0);
        frameLayout=(FrameLayout)findViewById(R.id.main_frameLayout);
        setFragment(new HomeFragment());
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        curuser_email = (TextView) navheaderView.findViewById(R.id.curuser_email);
        curuser_name = (TextView) navheaderView.findViewById(R.id.curuser_name);
        curuser_image=(CircleImageView) navheaderView.findViewById(R.id.curuser_image);
        curuser_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this,profileActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(Color.parseColor("#ffffff"));
        toggle.syncState();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.call_us){
            String telNumber="9876543210";
            final String data="tel:"+telNumber;
            Intent intent=new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(data));
            startActivity(intent);
        }
        else if(id== R.id.cart){
            Toast.makeText(this, "We are not active for medicine delivery yet, stay connected we will deliver soon", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.nav_logout:
                firebaseAuth.signOut();
                finish();
                final Intent intent = new Intent(WelcomeActivity.this , MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                startActivity(new Intent(WelcomeActivity.this,profileActivity.class));
                break;
            case R.id.nav_address:
                startActivity(new Intent(WelcomeActivity.this,AddAddressActivity.class));
                break;
            case R.id.nav_appointment:
                startActivity(new Intent(WelcomeActivity.this,yourBooking.class));
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

}
