package com.lenovo.doc;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    //private ImageView curserImage;
    //private TextView cname,cemail;
    private TextView curuser_email;
    private CircleImageView curuser_image;
    private ActionBarDrawerToggle toggle;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    //private FirebaseAuth mAuth;
    private FrameLayout frameLayout;
    public static Activity wel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        wel=this;
        firebaseAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        book_appoint=(LinearLayout)findViewById(R.id.book_appointment);
        buy_med=(LinearLayout)findViewById(R.id.buy_medicine);
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
        firestore.collection("USERS").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Glide.with(WelcomeActivity.this).load(documentSnapshot.getString("image").toString()).into(curuser_image);
                curuser_name.setText(documentSnapshot.getString("UserName"));
                curuser_email.setText(documentSnapshot.getString("UserEmail"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(WelcomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(WelcomeActivity.this);
            builder.setMessage("Are you sure want to exit the app?");

            builder.setTitle("Alert !");

            builder.setCancelable(false);
            builder
                    .setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    finish();
                                    //finish();
                                }
                            });
            builder
                    .setNegativeButton(
                            "No",
                            new DialogInterface
                                    .OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    dialog.cancel();
                                }
                            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();

            // Show the Alert Dialog box
            alertDialog.show();
            //super.onBackPressed();
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
            case R.id.nav_profile:
                Intent intent=new Intent(WelcomeActivity.this,profileActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this).toBundle());
                }
                else{
                    startActivity(intent);
                }
                break;
            case R.id.nav_appointment:
                Intent intent2=new Intent(WelcomeActivity.this,yourBooking.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent2, ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this).toBundle());
                }
                else{
                    startActivity(intent2);
                }
                break;
            case R.id.nav_health_book:
                Intent intent3=new Intent(WelcomeActivity.this,HealthBookActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent3, ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this).toBundle());
                }
                else{
                    startActivity(intent3);
                }
                break;
            case R.id.nav_connect:
                String telNumber="9876543210";
                final String data="tel:"+telNumber;
                Intent intent4=new Intent(Intent.ACTION_DIAL);
                intent4.setData(Uri.parse(data));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent4, ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this).toBundle());
                }
                else{
                    startActivity(intent4);
                }
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
