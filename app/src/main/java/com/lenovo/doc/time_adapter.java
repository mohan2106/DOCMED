package com.lenovo.doc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class time_adapter extends RecyclerView.Adapter<time_adapter.ViewHolder> {
    private List<time_model> itemList;
    private Context context;
    private int pos=100;

    public time_adapter(List<time_model> itemList, Context context,int time) {
        this.itemList = itemList;
        this.context = context;
        this.pos=time;
    }

    @NonNull
    @Override
    public time_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.time_interval,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final time_adapter.ViewHolder viewHolder, final int i) {
        time_model ne=itemList.get(i);
        viewHolder.time_chooser.setText(ne.getTime());
        viewHolder.traffic.setText("("+ne.getTraffic()+")");
        if(Integer.valueOf(ne.getTraffic())>10){
            viewHolder.traffic.setTextColor(Color.parseColor("#f50b0b"));
        }
        final int x=i;
        if(pos==x){
            viewHolder.time_chooser.setBackgroundResource(R.drawable.gradient_green);

        }
        viewHolder.time_chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos!=x){
                    v.setBackgroundResource(R.drawable.gradient_green);
                    pos=x;
                }
                else{
                    v.setBackgroundResource(R.drawable.gradient_color_background);
                    pos=100;
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button time_chooser;
        private TextView traffic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time_chooser=(Button)itemView.findViewById(R.id.time_interval_btn);
            traffic=(TextView)itemView.findViewById(R.id.trafic);
        }
    }
}
