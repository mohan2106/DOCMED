package com.lenovo.doc;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeasurementActivity extends AppCompatActivity {

    private CircleImageView blood,pulse,weight,sugar,height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Measurements");
        blood=(CircleImageView)findViewById(R.id.measure_1);
        pulse=(CircleImageView)findViewById(R.id.measure_2);
        weight=findViewById(R.id.measure_5);
        sugar=findViewById(R.id.measure_4);
        height=findViewById(R.id.measure_3);

        blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MeasurementActivity.this,BloodPressureActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MeasurementActivity.this).toBundle());
                }
                else{
                    startActivity(intent);
                }
            }
        });
        pulse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MeasurementActivity.this,HeightWeightPulseActivity.class);
                intent.putExtra("head","Pulse Rate");
                intent.putExtra("unit","beats/min");
                intent.putExtra("loc","PulaseRate");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MeasurementActivity.this).toBundle());
                }
                else{
                    startActivity(intent);
                }
            }
        });
        height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MeasurementActivity.this,HeightWeightPulseActivity.class);
                intent.putExtra("head","Height");
                intent.putExtra("unit","cm");
                intent.putExtra("loc","Height");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MeasurementActivity.this).toBundle());
                }
                else{
                    startActivity(intent);
                }
            }
        });

        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MeasurementActivity.this,HeightWeightPulseActivity.class);
                intent.putExtra("head","Weight");
                intent.putExtra("unit","Kg");
                intent.putExtra("loc","Weight");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MeasurementActivity.this).toBundle());
                }
                else{
                    startActivity(intent);
                }
            }
        });

        sugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MeasurementActivity.this,HeightWeightPulseActivity.class);
                intent.putExtra("head","Sugar Level");
                intent.putExtra("unit","mmol/l");
                intent.putExtra("loc","SugerLevel");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MeasurementActivity.this).toBundle());
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
}
