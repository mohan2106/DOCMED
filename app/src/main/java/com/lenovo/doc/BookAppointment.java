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
import android.widget.LinearLayout;
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
    private ArrayList<String> cityList;
    private LinearLayout locality;
    private doctor_adapter adapter;
    private String City;
    private String local;
    private TextView city_view,loc_text;
    private String flag;
    private EditText search_et;
    private ArrayList<String> localityList;
    private LatLng loc=null;
    private ProgressDialog bar;
    private Button sel_city,category;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        sel_city=(Button)findViewById(R.id.select_city);
        search_et=(EditText)findViewById(R.id.search_et);
        loc_text=(TextView)findViewById(R.id.locality_text);
        city_view=(TextView)findViewById(R.id.city_view);
        bar=new ProgressDialog(this);
        locality=(LinearLayout)findViewById(R.id.locality);
        category=(Button)findViewById(R.id.category);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Doctors");
        Intent i=getIntent();
        final String tv=i.getStringExtra("City_name");
        flag=i.getStringExtra("flag");
        String s2=i.getStringExtra("local");
        String categ=i.getStringExtra("category");
        if(Integer.valueOf(flag)==0){
            City=tv;
            local="Choose Locality";
        }
        else{
            local=tv;
            City=s2;
        }
        category.setText(categ);
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
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookAppointment.this,CategoryActivity.class);
                intent.putExtra("city",City);
                intent.putExtra("local",local);
                startActivity(intent);
            }
        });
        city_view.setText(City);
        loc_text.setText(local);

        cityList = new ArrayList<>();
        cityList.add("Jaipur");
        cityList.add("Patna");
        cityList.add("lucknow");
        cityList.add("Delhi");
        cityList.add("Mumbai");
        cityList.add("Bombay");
        cityList.add("Kota");
        cityList.add("Udaipur");
        cityList.add("Amritsar");
        cityList.add("Pune");
        cityList.add("Chennai");
        cityList.add("Hyderabad");
        cityList.add("Bengaluru");
        cityList.add("Kolkata");
        cityList.add("Guwahati");
        cityList.add("Dispur");
        cityList.add("Ahmedabad");
        cityList.add("Shilong");
        localityList=new ArrayList<>();
        localityList.add("Alaknanda");
        localityList.add("chittaranjan park");
        localityList.add("Defence Colony");
        localityList.add("Dwarka");
        localityList.add("East Of Kailash");
        localityList.add("Green Park");
        localityList.add("Hauz Khas");
        localityList.add("Janakpuri");
        localityList.add("Delhi ncr");
        localityList.add("Jasola Vihar");
        localityList.add("Malviya Nagar");
        localityList.add("Paschim Vihar");
        localityList.add("Pitam Pura");
        localityList.add("Uttam Nagar");
        localityList.add("Vasant Kunj");
        localityList.add("Vasant vihar");
        localityList.add("Vikaspuri");
        localityList.add("Red Fort");
        localityList.add("Jama Masjid");
        localityList.add("India gate");
        localityList.add("seceratariate");
        localityList.add("Sarita Vihar");

        sel_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookAppointment.this,searchActivity.class);
                intent.putExtra("text","Search City");
                intent.putExtra("local",local);
                intent.putExtra("cityName",City);
                intent.putExtra("city",cityList);
                intent.putExtra("flag","0");
                startActivity(intent);
                //startActivity(new Intent(BookAppointment.this,searchActivity.class));
            }
        });
        locality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookAppointment.this,searchActivity.class);
                intent.putExtra("text","Search Locality in "+City);
                intent.putExtra("local",local);
                intent.putExtra("cityName",City);
                intent.putExtra("city",localityList);
                intent.putExtra("flag","1");
                startActivity(intent);
            }
        });
        bar.setMessage("Loding doctors in "+City+"...");
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
