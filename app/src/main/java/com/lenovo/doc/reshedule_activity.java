package com.lenovo.doc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class reshedule_activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<horizontal_date> timeList;
    private horizontal_date_adapter adapter;
    private HorizontalCalendar horizontalCalendar;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private String name;
    private String fee;
    private String address;
    private String image;
    private String speciality;
    private String day;
    private String month;
    private String year;
    private String time;
    private Button btn;
    private ProgressDialog dialog;
    private String[] array;
    private String formattedDate;
    private  int noOfSlot;
    private TextView dt,tm;
    private List<time> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reshedule_activity);
        Intent in=getIntent();
        final String bookingId=in.getStringExtra("bookingId");
        final String id=in.getStringExtra("id");
        btn=(Button)findViewById(R.id.date_time_continue_button);
        recyclerView=findViewById(R.id.confirm_shedule);
        recyclerView.setHasFixedSize(true);
        dialog=new ProgressDialog(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        timeList=new ArrayList<>();
        adapter=new horizontal_date_adapter(timeList,this);
        recyclerView.setAdapter(adapter);
        dt=findViewById(R.id.schedule_date);
        tm=findViewById(R.id.schedule_time);
        list=new ArrayList<>();
        //dialog.setMessage("Loading slots...");
        //dialog.show();
        firestore.collection("Doctors").document("India").collection("Guwahati").document(id).collection("appointmentTime").document("noOfSlot").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                noOfSlot=Integer.valueOf(documentSnapshot.getString("no"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Schedule");
        //Date d = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);


        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 7);
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, 0);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(5)
                .dayNameFormat("EEE")
                .dayNumberFormat("dd")
                .monthFormat("MMM")
                .textSize(14f, 24f, 14f)
                .showDayName(true)
                .showMonthName(true)

                .build();
        Date d = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);

        SimpleDateFormat df = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            df = new SimpleDateFormat("dd-MMM-yyyy");
            formattedDate = df.format(d);
        }
        final String arr[]=formattedDate.split("-");
        Toast.makeText(this, arr[0], Toast.LENGTH_SHORT).show();
        adapter=new horizontal_date_adapter(timeList,this);
        recyclerView.setAdapter(adapter);
        //Toast.makeText(this, String.valueOf(list.get(0).getTime_range()), Toast.LENGTH_SHORT).show();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                final String arr[]=formattedDate.split("-");
                final String dat[]=String.valueOf(date).split(" ");
                dt.setText(dat[0]+" "+dat[2]+" "+dat[1]);
                tm.setText("Choose time");
                timeList.clear();
                String n=String.valueOf(date);
                array=n.split(" ");
                dialog.setMessage("Loading slots on "+date+"...");
                dialog.show();
                //Toast.makeText(BookingDateAndTime.this, arr[0]+" mohan", Toast.LENGTH_SHORT).show();
                firestore.collection("Doctors").document("India").collection("Guwahati").document(id).collection("appointmentTime").document("mon").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for(int i=0;i<noOfSlot;i++){
                            list.add(new time(documentSnapshot.getString(String.valueOf(i))));
                            if(i==(noOfSlot-1)){
                                final String arr[]=formattedDate.split("-");
                                //Toast.makeText(BookingDateAndTime.this, arr[0]+" mohan", Toast.LENGTH_SHORT).show();
                                firestore.collection("Doctors").document("India").collection("Guwahati").document(id).collection("appointmentTime").document("10").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        timeList.clear();
                                        for(int i=1;i<noOfSlot;i++){
                                            //time x=list;
                                            timeList.add(new horizontal_date(String.valueOf(list.get(i).getTime_range()),documentSnapshot.getString(String.valueOf(list.get(i).getTime_range()))));
                                            //Toast.makeText(BookingDateAndTime.this, list.get(5).getTime_range(), Toast.LENGTH_SHORT).show();
                                        }
                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(reshedule_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        //Toast.makeText(BookingDateAndTime.this, String.valueOf(list.size()), Toast.LENGTH_SHORT).show()
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(reshedule_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                adapter.notifyDataSetChanged();
                //Toast.makeText(BookingDateAndTime.this, array[2], Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onDateLongClicked(Date date, int position) {
                return super.onDateLongClicked(date, position);
            }
        });
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        });*/
        //final String din=d+"/"+m+"/"+y;
        //Toast.makeText(this, din, Toast.LENGTH_SHORT).show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp=tm.getText().toString();
                if(!TextUtils.isEmpty(temp)){
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

                                                firestore.collection("USERS").document(mAuth.getUid()).collection("Appointment").document(bookingId).update("Status","1","Date",dt.getText().toString(),"Time",tm.getText().toString());
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
                    Toast.makeText(reshedule_activity.this, "Please select time", Toast.LENGTH_SHORT).show();
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

    private class horizontal_date{
        String single_ate;
        String status;

        public horizontal_date(String single_ate,String status) {
            this.single_ate = single_ate;
            this.status=status;
        }

        public String getSingle_ate() {
            return single_ate;
        }

        public void setSingle_ate(String single_ate) {
            this.single_ate = single_ate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
    private class horizontal_date_adapter extends RecyclerView.Adapter<horizontal_date_adapter.ViewHolder>{

        private List<horizontal_date> itemList;
        private Context context;
        private int prev_pos=-1;

        public horizontal_date_adapter(List<horizontal_date> itemList, Context context) {
            this.itemList = itemList;
            this.context = context;
        }


        @NonNull
        @Override
        public horizontal_date_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v= LayoutInflater.from(context).inflate(R.layout.single_date_container,viewGroup,false);
            return new horizontal_date_adapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final horizontal_date_adapter.ViewHolder viewHolder, int i) {
            horizontal_date ne=itemList.get(i);
            //int position =i;
            viewHolder.lay.setTag(0);
            viewHolder.dateView.setText(ne.getSingle_ate());
            if(prev_pos==i){
                viewHolder.lay.setBackgroundColor(Color.WHITE);
                viewHolder.slot.setTextColor(Color.GREEN);
                viewHolder.dateView.setTextColor(Color.BLACK);
            }
            switch(ne.getStatus()){
                case "1":
                    viewHolder.slot.setText("not available");
                    viewHolder.slot.setTextColor(Color.RED);
                    break;
                case "0":
                    viewHolder.slot.setText("available");
                    viewHolder.lay.setBackground(getResources().getDrawable(R.drawable.button_corner));
                    viewHolder.slot.setTextColor(getResources().getColor(R.color.colorGreen));
                    viewHolder.dateView.setTextColor(getResources().getColor(R.color.colorBlack));
                    break;
                default:
                    break;
            }
            final String temp=ne.getStatus();
            final String temp_time=ne.getSingle_ate();
            viewHolder.lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(temp){
                        case "0":

                            int status2=(int)viewHolder.lay.getTag();
                            int prev=prev_pos;
                            switch(prev){
                                case -1:
                                    break;
                                default:
                                    notifyItemChanged(prev);
                            }
                            switch(status2){
                                case 1:
                                    prev_pos=-1;
                                    v.setBackground(getResources().getDrawable(R.drawable.button_corner));
                                    viewHolder.slot.setTextColor(getResources().getColor(R.color.colorGreen));
                                    viewHolder.dateView.setTextColor(getResources().getColor(R.color.colorBlack));
                                    viewHolder.lay.setTag(0);
                                    tm.setText("Choose time");
                                    break;
                                case 0:
                                    prev_pos = viewHolder.getAdapterPosition();
                                    v.setBackground(getResources().getDrawable(R.drawable.gradient_green));
                                    viewHolder.slot.setTextColor(getResources().getColor(R.color.colorWhite));
                                    viewHolder.dateView.setTextColor(getResources().getColor(R.color.colorWhite));
                                    viewHolder.lay.setTag(1);
                                    tm.setText(temp_time);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            Toast.makeText(context, "Not Available", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView dateView,slot;
            private ConstraintLayout lay;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                dateView=itemView.findViewById(R.id.Single_date_textview);
                slot=itemView.findViewById(R.id.slot_availability_text);
                lay=itemView.findViewById(R.id.single_date_container_contraint);
            }
        }
    }
    private class time{
        String time_range;

        public time(String time_range) {
            this.time_range = time_range;
        }

        public String getTime_range() {
            return time_range;
        }

        public void setTime_range(String time_range) {
            this.time_range = time_range;
        }
    }

}
