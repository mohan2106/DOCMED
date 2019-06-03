package com.lenovo.doc;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeightWeightPulseActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HeightWeightAdapter adapter;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private List<HeightWeightClass> itemList;
    private ProgressDialog pb;
    private Calendar c;
    private DatePickerDialog dbg;
    private TextView add_text;
    private EditText pb_upper;
    private TextView pb_date;
    private TextView un;
    private String d,m,y;
    private LinearLayout add_prep_text;
    private ConstraintLayout add_prep_layout;
    private Button pb_cancel,pb_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height_weight_pulse);
        Intent intent=getIntent();
        String head=intent.getStringExtra("head");
        String unit=intent.getStringExtra("unit");
        final String loc=intent.getStringExtra("loc");
        un=findViewById(R.id.unit_hwp);
        recyclerView=(RecyclerView)findViewById(R.id.all_hwp_recycler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(head);
        un.setText(unit);
        //pb_upper=(EditText)findViewById(R.id.upper_bound);
        add_text=(TextView)findViewById(R.id.add_text);
        add_text.setText("ADD "+head.toUpperCase());
        pb_upper=(EditText)findViewById(R.id.lowerBound_hwp);
        pb_date=(TextView)findViewById(R.id.date_chooser_hwp);
        pb_cancel=(Button)findViewById(R.id.cancel_btn_bp_hwp);
        pb_save=(Button)findViewById(R.id.save_btn_bp_hwp);
        add_prep_text=findViewById(R.id.add_prescription_text_hwp);
        add_prep_layout=findViewById(R.id.hwp_entry);
        add_prep_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_prep_layout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
        pb_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_prep_layout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        pb_upper.setHint("Enter "+head+" Here..");
        pb=new ProgressDialog(this);
        itemList=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new HeightWeightAdapter(itemList,this,unit);
        recyclerView.setAdapter(adapter);
        pb.setMessage("Fetching your previous "+head+" ...");
        pb.show();
        firestore.collection("USERS").document(mAuth.getUid()).collection("HealthBook").document("Measurement").collection(loc).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot: task.getResult()){
                        itemList.add(new HeightWeightClass(snapshot.getString("date"),snapshot.getString("value")));

                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(HeightWeightPulseActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                pb.dismiss();
            }
        });
        pb_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c= Calendar.getInstance();
                int year=c.get(Calendar.YEAR);
                int month=c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);
                dbg=new DatePickerDialog(HeightWeightPulseActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        pb_date.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        //date.setTextColor(Color.parseColor("#339f33"));
                        //date.setText("Change Date");
                        d=String.valueOf(dayOfMonth);
                        m=String.valueOf(month+1);
                        y=String.valueOf(year);
                    }
                },day,month,year);
                DatePicker dp = dbg.getDatePicker();
                dp.setMaxDate(c.getTimeInMillis());
                dbg.show();

            }
        });

        pb_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=pb_upper.getText().toString();
                //String b=pb_lower.getText().toString();
                String c=pb_date.getText().toString();
                if(!TextUtils.isEmpty(a)   &&  !c.equals("Choose Date")){
                    int x=Integer.valueOf(d);
                    int z=Integer.valueOf(m);
                    if(x/10 == 0){
                        x=x*10;
                    }
                    if(z/10 == 0){
                        z=z*10;
                    }
                    Map<String,Object> data=new HashMap<>();
                    data.put("value",a);
                    //data.put("bp_lower",b);
                    data.put("date",c);
                    data.put("priority_order",Integer.valueOf(y+String.valueOf(z)+String.valueOf(x)));
                    pb.setMessage("Uploading your data...");
                    pb.show();
                    firestore.collection("USERS").document(mAuth.getUid()).collection("HealthBook").document("Measurement").collection(loc).add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                finish();
                                startActivity(getIntent());
                            }
                            else{
                                Toast.makeText(HeightWeightPulseActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            pb.dismiss();
                        }
                    });
                }
                else{
                    Toast.makeText(HeightWeightPulseActivity.this, "All fields should be filledd", Toast.LENGTH_SHORT).show();
                }
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
