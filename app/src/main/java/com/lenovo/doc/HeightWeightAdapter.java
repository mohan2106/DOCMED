package com.lenovo.doc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HeightWeightAdapter extends RecyclerView.Adapter<HeightWeightAdapter.ViewHolder> {

    private List<HeightWeightClass> itemList;
    private Context context;
    private String unit;

    public HeightWeightAdapter(List<HeightWeightClass> itemList, Context context,String unit) {
        this.itemList = itemList;
        this.context = context;
        this.unit = unit;
    }

    @NonNull
    @Override
    public HeightWeightAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.single_height_weight_layout,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HeightWeightAdapter.ViewHolder viewHolder, int i) {
        HeightWeightClass ne=itemList.get(i);
        viewHolder.date_hwp.setText(ne.getDate());
        viewHolder.value_hwp.setText(ne.getValue());
        viewHolder.unit_hwp.setText(unit);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date_hwp,value_hwp,unit_hwp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date_hwp=itemView.findViewById(R.id.date_hwp);
            value_hwp=itemView.findViewById(R.id.value_hwp);
            unit_hwp=itemView.findViewById(R.id.unit_hwp);
        }
    }
}
