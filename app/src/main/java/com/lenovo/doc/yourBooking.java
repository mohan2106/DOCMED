package com.lenovo.doc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class yourBooking extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<your_booking_model> itemList;
    private your_booking_adapter adapter;
    //private ImageView mapImg;
    private FirebaseFirestore firebaseFirestore;
    private Toolbar toolbar;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_booking);
        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        firebaseFirestore=FirebaseFirestore.getInstance();
        //mapImg=(ImageView)findViewById(R.id.map_image);
        mAuth=FirebaseAuth.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fa=this;
        dialog=new ProgressDialog(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Your Appointment");
        itemList=new ArrayList<>();
        adapter=new your_booking_adapter(itemList,this);
        dialog.setMessage("Fetching your Order");
        dialog.show();
        recyclerView.setAdapter(adapter);
        firebaseFirestore.collection("USERS").document(mAuth.getUid()).collection("Appointment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        itemList.add(new your_booking_model(documentSnapshot.get("DrName").toString(),documentSnapshot.get("DrAddress").toString(),documentSnapshot.get("DrSpeciality").toString(),documentSnapshot.get("DrFee").toString(),documentSnapshot.get("DrImage").toString(),documentSnapshot.get("Status").toString(),documentSnapshot.get("Date").toString(),documentSnapshot.get("Time").toString(),documentSnapshot.get("lat").toString(),documentSnapshot.get("long").toString(),documentSnapshot.get("BookingId").toString(),documentSnapshot.get("id").toString(),documentSnapshot.getString("payment"),documentSnapshot.getString("rated")));

                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    String error=task.getException().getMessage();
                    Toast.makeText(yourBooking.this, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
