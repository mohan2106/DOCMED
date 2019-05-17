package com.lenovo.doc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class paymentActivity extends AppCompatActivity {
    private String name;
    private String fee;
    private String address;
    private String image;
    private String speciality;
    private String day;
    private String month;
    private String year;
    private TextView on;
    private String time;
    private ProgressDialog dialog;
    public static Activity payment_activity;
    private TextView tvname, tvfee, tvaddress, tvdate, tvtime, tvspeciality;
    private Button confirm_btn;
    private String bookingCount;
    private ImageView img;
    private EditText pat_name, pat_email, pat_contact;
    private LinearLayout pay_at_counter;
    private ImageView pay_online;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        payment_activity=this;
        Intent i = getIntent();
        name = i.getStringExtra("name");
        fee = i.getStringExtra("fee");
        address = i.getStringExtra("address");
        image = i.getStringExtra("image");
        speciality = i.getStringExtra("speciality");
        day = i.getStringExtra("day");
        month = i.getStringExtra("month");
        year = i.getStringExtra("year");
        time = i.getStringExtra("time");
        final String lat=i.getStringExtra("lat");
        final String lng=i.getStringExtra("long");
        final String id=i.getStringExtra("id");
        //final GeoPoint loc=i.getParcelableExtra("location");
        String[] parts=time.split(":");
        int initial=Integer.parseInt(parts[0]);
        int TID=0;
        if(initial<=8){
            initial+=12;
        }
        initial = initial*100;
        String[] parts2=parts[1].split("-");
        initial += Integer.parseInt(parts2[0]);
        final int data=initial;
        dialog = new ProgressDialog(this);
        pay_at_counter=(LinearLayout)findViewById(R.id.pay_at_counter);
        pay_online=(ImageView)findViewById(R.id.pay_online);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        tvname = (TextView) findViewById(R.id.name);
        on=(TextView)findViewById(R.id.orderNo);
        tvaddress = (TextView) findViewById(R.id.address);
        tvdate = (TextView) findViewById(R.id.date);
        tvtime = (TextView) findViewById(R.id.time);
        tvfee = (TextView) findViewById(R.id.fee);
        tvspeciality = (TextView) findViewById(R.id.speciality);
        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        img = (ImageView) findViewById(R.id.image);
        pat_name = (EditText) findViewById(R.id.name_patients);
        pat_email = (EditText) findViewById(R.id.email_patients);
        pat_contact = (EditText) findViewById(R.id.phone_patients);
        Glide.with(this)
                .load(image)
                .into(img);
        tvname.setText(name);
        tvspeciality.setText(speciality);
        tvdate.setText(day + "/" + month + "/" + year);
        tvaddress.setText(address);
        tvfee.setText(fee + "/-");
        tvtime.setText(time);
        //final String[] DocName = new String[1];


        firestore.collection("USERS").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    pat_name.setText(documentSnapshot.getString("UserName"));
                    pat_contact.setText(documentSnapshot.getString("UserPhone"));
                    pat_email.setText(documentSnapshot.getString("UserEmail"));
                } else {
                    Toast.makeText(paymentActivity.this, "document not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(paymentActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        pay_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(paymentActivity.this,payment_gateway.class);
                startActivity(intent);
            }
        });
        pay_at_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Approving Your Request...");
                dialog.show();
                firestore.collection("BookingCount").document("India").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String cn=documentSnapshot.getString("count");
                            final String DocName=String.valueOf(Integer.parseInt(cn)+1);
                            firestore.collection("BookingCount").document("India").update("count", DocName);
                            if(checkFielsd()>0){
                                Map<Object,String> Drdata=new HashMap<>();
                                Drdata.put("DrName",name);
                                Drdata.put("DrSpeciality",speciality);
                                Drdata.put("DrFee",fee);
                                Drdata.put("DrAddress",address);
                                Drdata.put("DrImage",image);
                                Drdata.put("Date",day+"/"+month+"/"+year);
                                Drdata.put("Time",time);
                                Drdata.put("Status","1");
                                Drdata.put("BookingId", DocName);
                                Drdata.put("lat",lat);
                                Drdata.put("long",lng);
                                Drdata.put("id",id);
                                //Drdata.put("latitude",String.valueOf(loc.getLatitude()));
                                //Drdata.put("longitude",String.valueOf(loc.getLongitude()));
                                firestore.collection("USERS").document(mAuth.getUid()).collection("Appointment").document(DocName).set(Drdata).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(paymentActivity.this, "Request Received", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(paymentActivity.this,thankingActivity.class));
                                        CategoryActivity.category_activity.finish();
                                        BookAppointment.book_appointment.finish();
                                        Doctor_profile.doctor_profile.finish();
                                        finish();
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(paymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        else{
                            Toast.makeText(paymentActivity.this, "document not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Map<String,Object> data_dr=new HashMap<>();
                data_dr.put("TID",data);
                data_dr.put("priority",2);
                //Map<Object,String> strData=new HashMap<>();
                data_dr.put("description",pat_name.getText().toString());
                data_dr.put("time",time);
                Toast.makeText(paymentActivity.this, String.valueOf(data), Toast.LENGTH_SHORT).show();
                firestore.collection("Doctors").document("India").collection("Guwahati").document(id).collection(day+"-"+month+"-"+year).document(mAuth.getUid()).set(data_dr).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(paymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                /*firestore.collection("Doctors").document("India").collection("Guwahati").document(id).collection(day+"-"+month+"-"+year).document(mAuth.getUid()).set(strData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(paymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*/

            }
        });





    }

    private int checkFielsd() {
        String nm=pat_name.getText().toString();
        String con=pat_contact.getText().toString();
        if(TextUtils.isEmpty(nm)){
            pat_name.setError("Patient name is required");
            pat_name.requestFocus();
            return 0;
        }
        if(TextUtils.isEmpty(con)){
            pat_contact.setError("Contact no is Required");
            pat_contact.requestFocus();
            return 0;
        }
        if(con.length()!=10){
            pat_contact.setError("Contact no must have length 10");
            pat_contact.requestFocus();
            return 0;
        }
        return 1;
    }
}