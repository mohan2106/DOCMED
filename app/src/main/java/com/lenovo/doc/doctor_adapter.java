package com.lenovo.doc;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.Model;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class doctor_adapter extends RecyclerView.Adapter<doctor_adapter.ViewHolder> {
    private List<doctor_details_class> itemList;
    private Context context;

    public doctor_adapter(List<doctor_details_class> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public doctor_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_doctor_profile,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final doctor_adapter.ViewHolder viewHolder, int i) {
        doctor_details_class ne=itemList.get(i);
        final String name=ne.getName();
        final String address=ne.getAddress();
        final String fee=ne.getFee();
        final String experience=ne.getExperiencce();
        final String Speciality=ne.getSpeciality();
        final String image=ne.getImage();
        final GeoPoint loc=ne.getLocation();
        final String id=ne.getId();
        //final com.lenovo.doc.Model model=new com.lenovo.doc.Model();
        //model.setGeoPoint(ne.getLocation());
        viewHolder.name.setText(ne.getName());
        viewHolder.address.setText(ne.getAddress());
        viewHolder.fee.setText("Fee: Rs."+ne.getFee());
        viewHolder.experience.setText(ne.getExperiencce()+" + Years exp");
        viewHolder.speciality.setText("speciality= "+ne.getSpeciality());
        Glide.with(context)
                .load(ne.getImage())
                .into(viewHolder.image);
        viewHolder.consult_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Doctor_profile.class);
                intent.putExtra("name",name);
                intent.putExtra("fee",fee);
                intent.putExtra("address",address);
                intent.putExtra("experience",experience);
                intent.putExtra("speciality",Speciality);
                intent.putExtra("image",image);
                intent.putExtra("lat",String.valueOf(loc.getLatitude()));
                intent.putExtra("long",String.valueOf(loc.getLongitude()));
                intent.putExtra("id",id);
                //intent.putExtra("location",model);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity)BookAppointment.act).toBundle());
                }
                else{
                    context.startActivity(intent);
                }
            }
        });
        final String nm=ne.getName();
        viewHolder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null,chooser=null;
                intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:"+loc.getLatitude()+","+loc.getLongitude()+"?q="+loc.getLatitude()+","+loc.getLongitude()+"("+nm+")&iwloc=A&hl=es"));
                chooser=Intent.createChooser(intent,"Launch Maps");
                context.startActivity(intent);
            }
        });
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Doctor_profile.class);
                intent.putExtra("name",name);
                intent.putExtra("fee",fee);
                intent.putExtra("address",address);
                intent.putExtra("experience",experience);
                intent.putExtra("speciality",Speciality);
                intent.putExtra("image",image);
                intent.putExtra("lat",String.valueOf(loc.getLatitude()));
                intent.putExtra("long",String.valueOf(loc.getLongitude()));
                intent.putExtra("id",id);
                //intent.putExtra("location",model);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity)BookAppointment.act).toBundle());
                }
                else{
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView name,speciality,experience,address,fee;
        private Button consult_btn;
        private ImageView location;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=(CircleImageView)itemView.findViewById(R.id.doctor_image);
            name=(TextView)itemView.findViewById(R.id.doctor_name);
            speciality=(TextView)itemView.findViewById(R.id.doctor_speciality);
            experience=(TextView)itemView.findViewById(R.id.doctor_experience);
            address=(TextView)itemView.findViewById(R.id.doctor_location);
            fee=(TextView)itemView.findViewById(R.id.doctor_fee);
            consult_btn=(Button)itemView.findViewById(R.id.doctor_consult);
            location=(ImageView)itemView.findViewById(R.id.doctor_map);
        }
    }
    public void filterList(List<doctor_details_class> filteredList) {
        itemList = filteredList;
        notifyDataSetChanged();
    }
}
