package com.lenovo.doc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.LatLng;

import java.util.ArrayList;
import java.util.List;

public class BookAppointment extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<doctor_details_class> itemList;
    private doctor_adapter adapter;
    private TextView city_view;
    private EditText search_et;
    private LatLng loc=null;
    private ProgressDialog bar;
    private Button sel_city;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        sel_city=(Button)findViewById(R.id.select_city);
        search_et=(EditText)findViewById(R.id.search_et);
        city_view=(TextView)findViewById(R.id.city_view);
        bar=new ProgressDialog(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Doctors");
        Intent i=getIntent();
        String city=i.getStringExtra("City_name");
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        city_view.setText(city);
        sel_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookAppointment.this,searchActivity.class));
            }
        });
        bar.setMessage("Loding doctors in "+city+" ...");
        bar.show();
        firebaseFirestore=FirebaseFirestore.getInstance();
        recyclerView=(RecyclerView)findViewById(R.id.dr_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList=new ArrayList<>();
        adapter=new doctor_adapter(itemList,this);
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("Doctors").document("India").collection("Guwahati").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        itemList.add(new doctor_details_class(documentSnapshot.get("Name").toString(),documentSnapshot.get("Address").toString(),documentSnapshot.get("Fee").toString(),documentSnapshot.get("Image").toString(),documentSnapshot.get("Experience").toString(),documentSnapshot.get("Speciality").toString(),(GeoPoint)documentSnapshot.get("Location")));
                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    String error=task.getException().getMessage();
                    Toast.makeText(BookAppointment.this, error, Toast.LENGTH_SHORT).show();
                }
                bar.dismiss();
            }
        });


    }
    private void filter(String text) {
        List<doctor_details_class> filteredList = new ArrayList<>();

        for (doctor_details_class item : itemList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
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
