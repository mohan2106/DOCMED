package com.lenovo.doc;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class rateReview extends AppCompatActivity {
    private TextView tv1,tv2;
    private RadioGroup rg1,rg2;
    private SmileRating smileRating;
    int reco=100;
    private Toolbar toolbar;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    int wait=100;
    private EditText review;
    private Button btn;
    private String formattedDate;
    private String userName;
    private String userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_review);
        Intent in=getIntent();
        final String id=in.getStringExtra("id");
        final String bookingId=in.getStringExtra("bookingId");
        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        btn=(Button)findViewById(R.id.submit_review);
        rg1=(RadioGroup)findViewById(R.id.rg1);
        rg2=(RadioGroup)findViewById(R.id.rg2);
        review=(EditText)findViewById(R.id.review_text);
        smileRating=(SmileRating)findViewById(R.id.smile_rating);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Give Feedback");
        Toast.makeText(this, bookingId + id, Toast.LENGTH_SHORT).show();
        firestore.collection("USERS").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userName=documentSnapshot.getString("UserName");
                userImage=documentSnapshot.getString("image");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(rateReview.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        //Log.i(TAG, "Bad");
                        break;
                    case SmileRating.GOOD:
                        //Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        //Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        //Log.i(TAG, "Okay");
                        break;
                    case SmileRating.TERRIBLE:
                        //Log.i(TAG, "Terrible");
                        break;

                }
                int level = smileRating.getRating();
                //Toast.makeText(rateReview.this,"Rating is: "+ String.valueOf(level), Toast.LENGTH_SHORT).show();
            }
        });

        //int level = smileRating.getRating();
        ///final int rate=level;
        //Toast.makeText(this, rate+" ", Toast.LENGTH_SHORT).show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rev=review.getText().toString();
                String rat=String.valueOf(smileRating.getRating());
                if(!TextUtils.isEmpty(rev) && !TextUtils.isEmpty(rat)){
                    firestore.collection("Doctors").document("India").collection("Guwahati").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String z=documentSnapshot.getString("rating");
                                Long b=(Long)documentSnapshot.get("totalReview");
                                Long c=(Long) documentSnapshot.get("recomendation");
                                if(z==null){
                                    z="0";
                                }
                                if(b==null){
                                    b= Long.valueOf(0);
                                }
                                if(c==null){
                                    c = Long.valueOf(0);
                                }
                                b += 1;
                                if(reco==1){
                                    c += 1;
                                }
                                Double a=Double.valueOf(z);
                                final int rate=smileRating.getRating();
                                a = ((a*(b-1)) + rate)/(b*1.0) ;
                                Toast.makeText(rateReview.this, String.valueOf(a), Toast.LENGTH_SHORT).show();
                                Map<String,Object> data=new HashMap<>();
                                data.put("rating",a.toString());
                                data.put("totalReview",b);
                                data.put("recomendation",c);
                                firestore.collection("Doctors").document("India").collection("Guwahati").document(id).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(rateReview.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                Date d = Calendar.getInstance().getTime();
                                //System.out.println("Current time => " + c);

                                SimpleDateFormat df = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    df = new SimpleDateFormat("dd-MMM-yyyy");
                                    formattedDate = df.format(d);
                                }


                                Map<String,Object> rated=new HashMap<>();
                                rated.put("rated","1");
                                firestore.collection("USERS").document(mAuth.getUid()).collection("Appointment").document(String.valueOf(bookingId)).update(rated).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Map<String,Object> review2=new HashMap<>();
                                        review2.put("Review",review.getText().toString());
                                        review2.put("Rating",String.valueOf(rate));
                                        //review2.put("UserId",mAuth.getUid());
                                        review2.put("Date",formattedDate);
                                        review2.put("userName",userName);
                                        review2.put("userImage",userImage);
                                        firestore.collection("Doctors").document("India").collection("Guwahati").document(id).collection("Review").document(mAuth.getUid()).set(review2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                startActivity(new Intent(rateReview.this,reviewThanks.class));
                                                finish();
                                                yourBooking.fa.finish();
                                                WelcomeActivity.wel.finish();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(rateReview.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(rateReview.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(rateReview.this, "document not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(rateReview.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                else{
                    Toast.makeText(rateReview.this, "All fields are mendatory", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //Toast.makeText(this, level, Toast.LENGTH_SHORT).show();


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
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_Yes1:
                if (checked){
                    reco=1;
                }
                break;
            case R.id.radio_no1:
                if (checked){
                    reco=0;
                }
                break;
            case R.id.radio_Yes2:
                if(checked){
                    wait=1;
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    rg1.setVisibility(View.VISIBLE);
                    rg2.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.radio_no2:
                if(checked){
                    wait=0;
                    tv1.setVisibility(View.GONE);
                    tv2.setVisibility(View.GONE);
                    rg1.setVisibility(View.GONE);
                    rg2.setVisibility(View.GONE);
                }
                break;
            case R.id.radio_Yes3:
                if(checked){
                    wait=0;
                }
                break;
            case R.id.radio_no3:
                if(checked){

                }
                break;
            case R.id.radio_half_hour:
                if(checked){
                   wait=1;
                }
                break;
            case R.id.radio_one_hour:
                if(checked){
                    wait=2;
                }
                break;
            case R.id.radio_two_hour:
                if(checked){
                    wait=3;
                }
                break;
            case R.id.radio_more_than_two_hour:
                if(checked){
                    wait=4;
                }
                break;
            default:
                break;


        }
    }

}
