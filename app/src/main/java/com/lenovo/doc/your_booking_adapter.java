package com.lenovo.doc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class your_booking_adapter extends RecyclerView.Adapter<your_booking_adapter.ViewModel> {
    private ArrayList<your_booking_model> itemList;
    private Context context;

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
        viewModel.status.setText(ne.getStatus());
        viewModel.date.setText(ne.getDate());
        viewModel.time.setText(ne.getTime());
        viewModel.manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewModel.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        private Button manage,cancel;
        public ViewModel(@NonNull View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.image);
            location=(ImageView)itemView.findViewById(R.id.map_image);
            status=(TextView)itemView.findViewById(R.id.status);
            name=(TextView)itemView.findViewById(R.id.name);
            speciality=(TextView)itemView.findViewById(R.id.speciality);
            address=(TextView)itemView.findViewById(R.id.address);
            fee=(TextView)itemView.findViewById(R.id.fee);
            date=(TextView)itemView.findViewById(R.id.date);
            time=(TextView)itemView.findViewById(R.id.time);
            manage=(Button)itemView.findViewById(R.id.reshedule);
            cancel=(Button)itemView.findViewById(R.id.cancel_booking);
        }
    }
}
