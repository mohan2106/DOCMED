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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.GeoPoint;

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
    private ArrayList<Button> btnList;
    private int pos=100;
    private LinearLayout call_for_detail;
    private TextView time_show;
    private int x=100;
    private DatePickerDialog dbg;
    private Button cont_btn;
    private String d,m,y;
    private int flag=99;
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
        time_show=(TextView)findViewById(R.id.time_show);
        cont_btn=(Button)findViewById(R.id.continue_btn);
        btnList=new ArrayList<>();
        btnList.add((Button)findViewById(R.id.button));
        btnList.add((Button)findViewById(R.id.button2));
        btnList.add((Button)findViewById(R.id.button3));
        btnList.add((Button)findViewById(R.id.button4));
        btnList.add((Button)findViewById(R.id.button5));
        btnList.add((Button)findViewById(R.id.button6));
        btnList.add((Button)findViewById(R.id.button7));
        btnList.add((Button)findViewById(R.id.button8));
        btnList.add((Button)findViewById(R.id.button9));
        btnList.add((Button)findViewById(R.id.button10));
        btnList.add((Button)findViewById(R.id.button11));
        btnList.add((Button)findViewById(R.id.button12));
        btnList.add((Button)findViewById(R.id.button13));
        btnList.add((Button)findViewById(R.id.button14));
        btnList.add((Button)findViewById(R.id.button15));
        btnList.add((Button)findViewById(R.id.button16));
        btnList.add((Button)findViewById(R.id.button17));
        btnList.add((Button)findViewById(R.id.button18));
        btnList.add((Button)findViewById(R.id.button19));
        btnList.add((Button)findViewById(R.id.button20));
        btnList.add((Button)findViewById(R.id.button21));
        btnList.add((Button)findViewById(R.id.button22));
        btnList.add((Button)findViewById(R.id.button23));
        btnList.add((Button)findViewById(R.id.button24));
        for(int i=0;i<24;i++){
            //x=i;
            final int temp=i;
            btnList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    x=temp;
                    if(x==flag){
                        v.setBackgroundResource(R.drawable.gradient_color_background);
                        time_show.setText("Time Not Choosen");
                        flag=99;
                        x=98;
                    }
                    else{
                        v.setBackgroundResource(R.drawable.gradient_green);
                        time_show.setText(btnList.get(x).getText());
                        if(flag<24){
                            btnList.get(flag).setBackgroundResource(R.drawable.gradient_color_background);
                        }
                        flag=x;
                    }
                    //Toast.makeText(Doctor_profile.this, String.valueOf(x), Toast.LENGTH_SHORT).show();
                }
            });
        }
        Intent i=getIntent();
        final String name=i.getStringExtra("name");
        final String fee=i.getStringExtra("fee");
        String experience=i.getStringExtra("experience");
        final String address=i.getStringExtra("address");
        final String speciality=i.getStringExtra("speciality");
        final String image=i.getStringExtra("image");
        //final GeoPoint loc=i.getParcelableExtra("location");
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
                        d=String.valueOf(dayOfMonth);
                        m=String.valueOf(month+1);
                        y=String.valueOf(year);
                    }
                },day,month,year);
                DatePicker dp = dbg.getDatePicker();
                dp.setMinDate(c.getTimeInMillis());
                dbg.show();

            }
        });
        //final com.lenovo.doc.Model model=new com.lenovo.doc.Model();
        //model.setGeoPoint(loc);
        cont_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(x<24){
                    Intent intent=new Intent(Doctor_profile.this,paymentActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("fee",fee);
                    intent.putExtra("address",address);
                    intent.putExtra("image",image);
                    intent.putExtra("speciality",speciality);
                    intent.putExtra("day",d);
                    intent.putExtra("month",m);
                    intent.putExtra("year",y);
                    intent.putExtra("time",btnList.get(x).getText());
                    //intent.putExtra("location",model);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Doctor_profile.this, "Choose Time", Toast.LENGTH_SHORT).show();
                }

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
