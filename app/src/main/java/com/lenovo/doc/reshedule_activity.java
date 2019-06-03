package com.lenovo.doc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class reshedule_activity extends AppCompatActivity {
    private Button date;
    private Calendar c;
    private ArrayList<Button> btnList;
    private int pos=100;
    //private LinearLayout call_for_detail;
    private TextView time_show;
    private int x=100;
    private DatePickerDialog dbg;
    private String d,m,y;
    private LinearLayout ll;
    private Button confirm_shedule;
    private int flag=99;
    private TextView date_view;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reshedule_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent in=getIntent();
        final String bookingId=in.getStringExtra("bookingId");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        confirm_shedule=(Button)findViewById(R.id.confirm_schedule);
        toolbar.setTitle("Profile");
        ll=(LinearLayout)findViewById(R.id.linearLayout3);
        ll.setVisibility(View.GONE);
        //call_for_detail=(LinearLayout)findViewById(R.id.call_for_details);
        time_show=(TextView)findViewById(R.id.time_show);
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
        date_view=(TextView)findViewById(R.id.date_view);
        date=(Button)findViewById(R.id.choose_date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c=Calendar.getInstance();
                int year=c.get(Calendar.YEAR);
                int month=c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);
                dbg=new DatePickerDialog(reshedule_activity.this, new DatePickerDialog.OnDateSetListener() {
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
        //final String din=d+"/"+m+"/"+y;
        //Toast.makeText(this, din, Toast.LENGTH_SHORT).show();
        confirm_shedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(d!=null && x<24){
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(reshedule_activity.this);
                    builder.setMessage("This will cancel your previous booking and schedule new booking with new date and time. Are you sure want to cancel your previous Booking?");

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

                                                firestore.collection("USERS").document(mAuth.getUid()).collection("Appointment").document(bookingId).update("Status","1","Date",d+"/"+m+"/"+y,"Time",time_show.getText().toString());
                                                //notifyDataSetChanged();
                                                startActivity(new Intent(reshedule_activity.this,yourBooking.class));
                                                finish();
                                                yourBooking.fa.finish();

                                            //yourBooking.finish();
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
                }
                else{
                    Toast.makeText(reshedule_activity.this, "Time and date must be required..", Toast.LENGTH_SHORT).show();
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
