package com.lenovo.doc;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Doctor_profile extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Button date;
    private TextView pr_name,special,addr,exper,fee;
    private Calendar c;
    private int pos=100;
    private LinearLayout call_for_detail;
    private DatePickerDialog dbg;
    private TextView date_view;
    //private List<time_model> itemList;
    private CircleImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        call_for_detail=(LinearLayout)findViewById(R.id.call_for_details);
        pr_name=(TextView)findViewById(R.id.profile_name);
        special=(TextView)findViewById(R.id.profile_category);
        addr=(TextView)findViewById(R.id.profile_address);
        exper=(TextView)findViewById(R.id.profile_experience);
        img=(CircleImageView)findViewById(R.id.circleImageView);
        Intent i=getIntent();
        String name=i.getStringExtra("name");
        String fee=i.getStringExtra("fee");
        String experience=i.getStringExtra("experience");
        String address=i.getStringExtra("address");
        String speciality=i.getStringExtra("speciality");
        String image=i.getStringExtra("image");
        Glide.with(this)
                .load(image)
                .into(img);
        pr_name.setText(name);
        special.setText(speciality);
        addr.setText(address);
        exper.setText(experience+" +exp");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Profile");
        date_view=(TextView)findViewById(R.id.date_view);
        date=(Button)findViewById(R.id.choose_date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c=Calendar.getInstance();
                int year=c.get(Calendar.YEAR);
                int month=c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);
                dbg=new DatePickerDialog(Doctor_profile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date_view.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        date_view.setTextColor(Color.parseColor("#339f33"));
                        date.setText("Change Date");
                    }
                },day,month,year);
                DatePicker dp = dbg.getDatePicker();
                dp.setMinDate(c.getTimeInMillis());
                dbg.show();

            }
        });
        call_for_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telNumber="9876543210";
                final String data="tel:"+telNumber;
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(data));
                startActivity(intent);
            }
        });
        /*recyclerView=(RecyclerView)findViewById(R.id.horizontal_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList=new ArrayList<>();
        adapter=new time_adapter(itemList,this,pos);
        itemList.add(new time_model("4:30-5:00","15"));
        itemList.add(new time_model("4:00-4:30","13"));
        itemList.add(new time_model("3:30-4:00","12"));
        itemList.add(new time_model("3:00-3:30","15"));
        itemList.add(new time_model("2:30-3:00","11"));
        itemList.add(new time_model("2:00-2:30","7"));
        itemList.add(new time_model("12:30-01:00","2"));
        itemList.add(new time_model("11:30-12:00","5"));
        itemList.add(new time_model("11:00-11:30","8"));
        itemList.add(new time_model("10:30-11:00","11"));
        itemList.add(new time_model("10:00-10:30","7"));
        itemList.add(new time_model("9:30-10:00","5"));
        itemList.add(new time_model("9:00-9:30","8"));
        recyclerView.setAdapter(adapter);*/
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
