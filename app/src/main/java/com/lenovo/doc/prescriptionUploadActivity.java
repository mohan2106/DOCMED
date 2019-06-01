package com.lenovo.doc;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class prescriptionUploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE=1;

    private LinearLayout add_prep_text;
    private ConstraintLayout add_prep_layout;
    private RecyclerView all_prep;
    private Button cancel,save;
    private ImageView prescription_image;
    private EditText name,add,reason;
    private TextView date,time;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private Uri imageUri;
    private RecyclerView all_prep_recycler;
    private prescriptionAdapter adapter;
    private Calendar c;
    private DatePickerDialog dbg;
    private StorageReference mStorage= FirebaseStorage.getInstance().getReference().child("UserPrescription");
    private ProgressDialog pb;
    private String d,m,y;
    private List<prescription_class> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_upload);
        add_prep_text=findViewById(R.id.add_prescription_text);
        add_prep_layout=findViewById(R.id.add_prep_layout);
        all_prep=findViewById(R.id.all_prescription_recycler_view);
        cancel=findViewById(R.id.add_prep_cancel_btn);
        save=findViewById(R.id.add_prep_save_btn);
        prescription_image=findViewById(R.id.add_prescription_image);
        name=findViewById(R.id.add_prescription_dr_name);
        all_prep_recycler=(RecyclerView)findViewById(R.id.all_prescription_recycler_view);
        add=findViewById(R.id.add_prescription_dr_address);
        reason=findViewById(R.id.add_prescription_reason);
        date=findViewById(R.id.add_prescription_date);
        pb=new ProgressDialog(this);
        //time=findViewById(R.id.add_prep_choose_time);
        add_prep_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_prep_layout.setVisibility(View.VISIBLE);
                all_prep.setVisibility(View.GONE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_prep_layout.setVisibility(View.GONE);
                all_prep.setVisibility(View.VISIBLE);
            }
        });
        prescription_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c= Calendar.getInstance();
                int year=c.get(Calendar.YEAR);
                int month=c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);
                dbg=new DatePickerDialog(prescriptionUploadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth+"/"+(month+1)+"/"+year);
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

        /*time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(prescriptionUploadActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Prescription");

        all_prep_recycler.setHasFixedSize(true);
        all_prep_recycler.setLayoutManager(new LinearLayoutManager(this));
        itemList=new ArrayList<>();
        adapter=new prescriptionAdapter(itemList,this);
        all_prep_recycler.setAdapter(adapter);
        pb.setMessage("Loading prescription...");
        pb.show();
        firestore.collection("USERS").document(mAuth.getUid()).collection("HealthBook").document("Prescription").collection("India").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot: task.getResult()){
                        itemList.add(new prescription_class(snapshot.getString("DrName"),snapshot.getString("ConsultDate"),snapshot.getString("consultReason"),snapshot.getString("prescription_image")));

                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(prescriptionUploadActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                pb.dismiss();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String dr_name=name.getText().toString();
                final String dr_address=add.getText().toString();
                final String dr_reason=reason.getText().toString();
                final String id=String.valueOf(System.currentTimeMillis());
                if((imageUri != null) && (!TextUtils.isEmpty(dr_name)) && (!TextUtils.isEmpty(dr_address)) && (!TextUtils.isEmpty(dr_reason)) && (date.getText().toString() != "choose date") ){
                    //String usserPassword=password.getText().toString();
                    pb.setMessage("Uploading your data...");
                    pb.show();
                    final FirebaseUser user = mAuth.getCurrentUser();
                    final StorageReference user_profile=mStorage.child(user+".jpg");
                    user_profile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            user_profile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String image_download = uri.toString();
                                    //String token_id = FirebaseInstanceId.getInstance().getToken();
                                    int a= Integer.parseInt(m);
                                    if(a/10 == 0){
                                        a=a*10;
                                    }
                                    int b=Integer.parseInt(d);
                                    if(b/10 == 0){
                                        b=b*10;
                                    }

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("DrName", dr_name);
                                    data.put("DrAddress", dr_address);
                                    data.put("prescription_image", image_download);
                                    data.put("ConsultDate", date.getText().toString());
                                    data.put("consultReason", dr_reason);
                                    data.put("priorityOrder",Integer.valueOf(y+String.valueOf(a)+String.valueOf(b)));
                                    //data.put("id",id);
                                    firestore.collection("USERS").document(mAuth.getUid()).collection("HealthBook").document("Prescription").collection("India").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(prescriptionUploadActivity.this, "Prescription uploaded", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(prescriptionUploadActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            pb.dismiss();
                                            finish();
                                            startActivity(getIntent());
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(prescriptionUploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                }
                else{
                    Toast.makeText(prescriptionUploadActivity.this, "Enter all fields", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE){
            imageUri=data.getData();
            prescription_image.setImageURI(imageUri);
        }
    }
}
