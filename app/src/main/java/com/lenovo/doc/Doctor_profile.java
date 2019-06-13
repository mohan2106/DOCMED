package com.lenovo.doc;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
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

import de.hdodenhof.circleimageview.CircleImageView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class Doctor_profile extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView reviewRecycler;
    private reviewAdapter review_adapter;
    private List<review_model> review_list;
    private Button date;
    private TextView pr_name,special,addr,exper,fee;
    private Calendar c;
    private ArrayList<Button> btnList;
    private int pos=100;
    private LinearLayout call_for_detail;
    private TextView time_show;
    private int x=100;
    private DatePickerDialog dbg;
    private ImageView mapImg;
    public static Activity doctor_profile;
    private Button cont_btn;
    private String d,m,y;
    private int flag=99;
    private LinearLayout lay;
    private TextView date_view;
    private ConstraintLayout v;
    //private List<time_model> itemList;
    private CircleImageView img;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //private HorizontalCalendar horizontalCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        Intent i=getIntent();
        final String name=i.getStringExtra("name");
        final String fee=i.getStringExtra("fee");
        String experience=i.getStringExtra("experience");
        final String address=i.getStringExtra("address");
        final String speciality=i.getStringExtra("speciality");
        final String image=i.getStringExtra("image");
        final String lat=i.getStringExtra("lat");
        final String lng=i.getStringExtra("long");
        final String id=i.getStringExtra("id");
        call_for_detail=(LinearLayout)findViewById(R.id.call_for_details);
        doctor_profile=this;
        v=findViewById(R.id.view_review);
        //v.getViewTreeObserver()
          //      .addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(v));
        pr_name=(TextView)findViewById(R.id.profile_name);
        lay=findViewById(R.id.no_review_layout);
        special=(TextView)findViewById(R.id.profile_category);
        addr=(TextView)findViewById(R.id.profile_address);
        exper=(TextView)findViewById(R.id.profile_experience);
        img=(CircleImageView)findViewById(R.id.circleImageView);
        mapImg=(ImageView)findViewById(R.id.profile_map);
        time_show=(TextView)findViewById(R.id.time_show);
        reviewRecycler=(RecyclerView)findViewById(R.id.public_review);
        reviewRecycler.setHasFixedSize(true);
        reviewRecycler.setLayoutManager(new LinearLayoutManager(this));
        review_list=new ArrayList<>();
        firestore.collection("Doctors").document("India").collection("Guwahati").document(id).collection("Review").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot snapshot:task.getResult()){
                        review_list.add(new review_model(snapshot.getString("userName"),snapshot.getString("userImage"),snapshot.getString("Date"),snapshot.getString("Rating"),snapshot.getString("Review")));
                        review_adapter.notifyDataSetChanged();
                    }
                    if(review_list.size() == 0){
                        lay.setVisibility(View.VISIBLE);
                    }
                    else{
                        lay.setVisibility(View.GONE);
                    }
                }
                else{
                    Toast.makeText(Doctor_profile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        review_adapter=new reviewAdapter(review_list,this);
        reviewRecycler.setAdapter(review_adapter);

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
        for(int j=0;j<24;j++){
            //x=i;
            final int temp=j;
            btnList.get(j).setOnClickListener(new View.OnClickListener() {
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
        final String[] no=new String[1];
        firestore.collection("BookingCount").document("India").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    //on.setText(documentSnapshot.getString("count"));
                    //Toast.makeText(paymentActivity.this, on.getText().toString(), Toast.LENGTH_SHORT).show();
                    no[0]=documentSnapshot.getString("count");
                    //DocName[0] =String.valueOf(Integer.parseInt(documentSnapshot.getString("count"))+1);
                }
                else{
                    //Toast.makeText(paymentActivity.this, "document not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Toast.makeText(this, lat+"  "+lng, Toast.LENGTH_SHORT).show();
        mapImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null,chooser=null;
                intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:"+lat+","+lng+"?q="+lat+","+lng+"("+name+")&iwloc=A&hl=es"));
                chooser=Intent.createChooser(intent,"Launch Maps");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Doctor_profile.this).toBundle());
                }
                else{
                    startActivity(intent);
                }
            }
        });
        //final com.lenovo.doc.Model model=new com.lenovo.doc.Model();
        //model.setGeoPoint(loc);
        cont_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(x<24 && d!=null){
                    Intent intent=new Intent(Doctor_profile.this,BookingDateAndTime.class);
                    intent.putExtra("name",name);
                    intent.putExtra("fee",fee);
                    intent.putExtra("address",address);
                    intent.putExtra("image",image);
                    intent.putExtra("speciality",speciality);
                    //intent.putExtra("day",d);
                    //intent.putExtra("month",m);
                    //intent.putExtra("year",y);
                    intent.putExtra("lat",lat);
                    intent.putExtra("count",no[0]);
                    intent.putExtra("long",lng);
                    //intent.putExtra("time",btnList.get(x).getText());
                    intent.putExtra("id",id);
                    //intent.putExtra("location",model);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Doctor_profile.this).toBundle());
                }
                else{
                    startActivity(intent);
                }
                /*}
                else{
                    Toast.makeText(Doctor_profile.this, "Date and Time must required", Toast.LENGTH_SHORT).show();
                }*/

            }
        });
        call_for_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telNumber="9876543210";
                final String data="tel:"+telNumber;
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(data));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Doctor_profile.this).toBundle());
                }
                else{
                    startActivity(intent);
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
    private class review_model{
        private String userName,userImage,userDate,userRaing,userReview;

        public review_model(String userName, String userImage, String userDate, String userRaing, String userReview) {
            this.userName = userName;
            this.userImage = userImage;
            this.userDate = userDate;
            this.userRaing = userRaing;
            this.userReview = userReview;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getUserDate() {
            return userDate;
        }

        public void setUserDate(String userDate) {
            this.userDate = userDate;
        }

        public String getUserRaing() {
            return userRaing;
        }

        public void setUserRaing(String userRaing) {
            this.userRaing = userRaing;
        }

        public String getUserReview() {
            return userReview;
        }

        public void setUserReview(String userReview) {
            this.userReview = userReview;
        }
    }
    private class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.ViewHolder>{
        private List<review_model> itemList;
        private Context context;

        public reviewAdapter(List<review_model> itemList, Context context) {
            this.itemList = itemList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v= LayoutInflater.from(context).inflate(R.layout.single_review_content,viewGroup,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            review_model ne=itemList.get(i);
            viewHolder.ratingBar.setRating(Integer.valueOf(ne.getUserRaing()));
            viewHolder.userReview.setText(ne.getUserReview());
            viewHolder.userDate.setText(ne.getUserDate());
            viewHolder.userName.setText(ne.getUserName());
            viewHolder.userRating.setText(ne.getUserRaing());
            Glide.with(context).load(ne.getUserImage()).into(viewHolder.userImage);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView userImage;
            private TextView userName,userDate,userRating,userReview;
            private RatingBar ratingBar;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                userImage=itemView.findViewById(R.id.review_user_image);
                userName=itemView.findViewById(R.id.review_user_name);
                userDate=itemView.findViewById(R.id.review_date);
                userRating=itemView.findViewById(R.id.review_rating_int);
                ratingBar=itemView.findViewById(R.id.review_rating);
                userReview=itemView.findViewById(R.id.review_review);

            }
        }
    }
    private static class OnViewGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private final static int maxHeight = 600;
        private View view;

        public OnViewGlobalLayoutListener(View view) {
            this.view = view;
        }

        @Override
        public void onGlobalLayout() {
            if (view.getHeight() > maxHeight)
                view.getLayoutParams().height = maxHeight;
        }
    }
}
