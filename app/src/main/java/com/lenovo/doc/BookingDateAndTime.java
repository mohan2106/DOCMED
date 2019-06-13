package com.lenovo.doc;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class BookingDateAndTime extends AppCompatActivity {
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
    public static Activity dat;
    private Button btn;
    private ProgressDialog dialog;
    private String[] array;
    private String formattedDate;
    private  int noOfSlot=-1;
    private TextView dt,tm;
    private List<time> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_date_and_time);
        Intent i = getIntent();
        name = i.getStringExtra("name");
        fee = i.getStringExtra("fee");
        address = i.getStringExtra("address");
        image = i.getStringExtra("image");
        speciality = i.getStringExtra("speciality");
        //day = i.getStringExtra("day");
        //month = i.getStringExtra("month");
        //year = i.getStringExtra("year");
        //time = i.getStringExtra("time");
        final String lat=i.getStringExtra("lat");
        final String lng=i.getStringExtra("long");
        final String id=i.getStringExtra("id");
        dat=this;
        btn=(Button)findViewById(R.id.date_time_continue_button);
        recyclerView=findViewById(R.id.single_time_recycler);
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
                Toast.makeText(BookingDateAndTime.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        //Toast.makeText(this, arr[0], Toast.LENGTH_SHORT).show();
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
                if(noOfSlot == -1){
                    firestore.collection("Doctors").document("India").collection("Guwahati").document(id).collection("appointmentTime").document("noOfSlot").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            noOfSlot=Integer.valueOf(documentSnapshot.getString("no"));
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
                                                    Toast.makeText(BookingDateAndTime.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                    //Toast.makeText(BookingDateAndTime.this, String.valueOf(list.size()), Toast.LENGTH_SHORT).show()
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(BookingDateAndTime.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BookingDateAndTime.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
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
                                            Toast.makeText(BookingDateAndTime.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            //Toast.makeText(BookingDateAndTime.this, String.valueOf(list.size()), Toast.LENGTH_SHORT).show()
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BookingDateAndTime.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                adapter.notifyDataSetChanged();
                //Toast.makeText(BookingDateAndTime.this, array[2], Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onDateLongClicked(Date date, int position) {
                return super.onDateLongClicked(date, position);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comp=tm.getText().toString();
                if(!comp.equals("Choose time")){
                    Intent intent=new Intent(BookingDateAndTime.this,paymentActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("fee",fee);
                    intent.putExtra("address",address);
                    intent.putExtra("image",image);
                    intent.putExtra("speciality",speciality);
                    //intent.putExtra("day",day);
                    //intent.putExtra("month",month);
                    //intent.putExtra("year",year);
                    intent.putExtra("date",dt.getText().toString());
                    intent.putExtra("lat",lat);
                    //intent.putExtra("count",);
                    intent.putExtra("long",lng);
                    intent.putExtra("time",tm.getText().toString());
                    intent.putExtra("id",id);
                    //intent.putExtra("location",model);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(BookingDateAndTime.this).toBundle());
                    }
                    else{
                        startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(BookingDateAndTime.this, "Please Choose time of meeting", Toast.LENGTH_LONG).show();
                }

            }
        });
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
            return new ViewHolder(v);
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
