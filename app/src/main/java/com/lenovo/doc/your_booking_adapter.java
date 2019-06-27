package com.lenovo.doc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class your_booking_adapter extends RecyclerView.Adapter<your_booking_adapter.ViewModel> {
    private ArrayList<your_booking_model> itemList;
    private Context context;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    public your_booking_adapter(ArrayList<your_booking_model> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public your_booking_adapter.ViewModel onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(context).inflate(R.layout.your_booking_single_layout,viewGroup,false);
        return new ViewModel(v);
    }

    @Override
    public void onBindViewHolder(@NonNull your_booking_adapter.ViewModel viewModel, int i) {
        your_booking_model ne=itemList.get(i);
        viewModel.name.setText(ne.getName());
        viewModel.speciality.setText(ne.getSpeciality());
        viewModel.address.setText(ne.getAddress());
        viewModel.fee.setText(ne.getFee());
        //Toast.makeText(context, ne.getStatus(), Toast.LENGTH_SHORT).show();
        switch (ne.getStatus()){
            case "1":
                switch (ne.getPaymentStatus()){
                    case "0":
                        viewModel.paymentStatus.setVisibility(View.GONE);
                        viewModel.paid.setText("Pay at counter");
                        viewModel.paid.setTextColor(Color.BLACK);
                        break;
                    case "1":
                        viewModel.paymentStatus.setVisibility(View.VISIBLE);
                        viewModel.paid.setText("Paid");
                }
                viewModel.status.setText("Appointment on: "+ne.getDate());
                viewModel.status.setTextColor(Color.parseColor("#000000"));
                viewModel.date.setTextColor(Color.parseColor("#000000"));
                viewModel.time.setTextColor(Color.parseColor("#000000"));
                break;
            case "0":
                viewModel.paymentStatus.setVisibility(View.GONE);
                viewModel.paid.setVisibility(View.GONE);
                viewModel.status.setText("Cancelled");
                viewModel.status.setTextColor(Color.parseColor("#f50b0b"));
                viewModel.date.setTextColor(Color.parseColor("#f50b0b"));
                viewModel.time.setTextColor(Color.parseColor("#f50b0b"));
                viewModel.cancel.setVisibility(View.GONE);
                viewModel.manage.setVisibility(View.GONE);
                break;
            case "2":
                viewModel.paymentStatus.setVisibility(View.GONE);
                viewModel.paid.setVisibility(View.GONE);
                switch(ne.getRated()){
                    case "0":
                        viewModel.status.setText("Meeting Done");
                        viewModel.status.setTextColor(Color.parseColor("#178119"));
                        viewModel.date.setTextColor(Color.parseColor("#178119"));
                        viewModel.time.setTextColor(Color.parseColor("#178119"));
                        viewModel.cancel.setVisibility(View.GONE);
                        viewModel.manage.setVisibility(View.GONE);
                        viewModel.rate.setVisibility(View.VISIBLE);
                        break;
                    case "1":
                        viewModel.status.setText("Meeting Done");
                        viewModel.status.setTextColor(Color.parseColor("#178119"));
                        viewModel.date.setTextColor(Color.parseColor("#178119"));
                        viewModel.time.setTextColor(Color.parseColor("#178119"));
                        viewModel.cancel.setVisibility(View.GONE);
                        viewModel.manage.setVisibility(View.GONE);
                        viewModel.rate.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }

            default:
                break;
        }
        viewModel.date.setText(ne.getDate());
        viewModel.time.setText(ne.getTime());
        Glide.with(context)
                .load(ne.getImage())
                .into(viewModel.image);
        final String date=ne.getDate();
        final String time=ne.getTime();
        final String bookingId=ne.getBookingId();
        final String id=ne.getId();
        viewModel.manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,reshedule_activity.class);
                intent.putExtra("bookingId",bookingId);
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });
        viewModel.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "cancel button clicked", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(context);
                builder.setMessage("Are you sure want to cancel your Appointment ?");

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
                                        firestore.collection("USERS").document(mAuth.getUid()).collection("Appointment").document(bookingId).update("Status","0");
                                        //notifyDataSetChanged();
                                        context.startActivity(new Intent(context,yourBooking.class));
                                        ((Activity)context).finish();
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
        });
        viewModel.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,rateReview.class);
                intent.putExtra("id",id);
                intent.putExtra("bookingId",bookingId);
                context.startActivity(intent);
            }
        });
        final String lat=ne.getLat();
        final String lng=ne.getLng();
        final String nm=ne.getName();
        viewModel.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null,chooser=null;
                intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:"+lat+","+lng+"?q="+lat+","+lng+"("+nm+")&iwloc=A&hl=es"));
                chooser=Intent.createChooser(intent,"Launch Maps");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewModel extends RecyclerView.ViewHolder {
        private ImageView image,location;
        private TextView status,name,speciality,address,fee,date,time;
        private TextView manage,cancel;
        private Button rate;
        private ImageView paymentStatus;
        private TextView paid;
        public ViewModel(@NonNull View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.image);
            location=(ImageView)itemView.findViewById(R.id.map_image);
            rate=(Button)itemView.findViewById(R.id.rate_review);
            status=(TextView)itemView.findViewById(R.id.status);
            name=(TextView)itemView.findViewById(R.id.name);
            speciality=(TextView)itemView.findViewById(R.id.speciality);
            address=(TextView)itemView.findViewById(R.id.address);
            fee=(TextView)itemView.findViewById(R.id.fee);
            date=(TextView)itemView.findViewById(R.id.date);
            time=(TextView)itemView.findViewById(R.id.time);
            manage=(TextView)itemView.findViewById(R.id.manageBtn);
            cancel=(TextView)itemView.findViewById(R.id.cancelBtn);
            paymentStatus=itemView.findViewById(R.id.paymentStatusImage);
            paid=itemView.findViewById(R.id.paymentStatusText);

        }
    }
}
