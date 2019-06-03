package com.lenovo.doc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class bp_adapter extends RecyclerView.Adapter<bp_adapter.ViewHolder> {
    private List<bp_class> itemList;
    private Context context;

    public bp_adapter(List<bp_class> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.single_bloode_pressure,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        bp_class ne=itemList.get(i);
        viewHolder.date.setText(ne.getDate());
        viewHolder.up.setText(ne.getUb());
        viewHolder.lo.setText(ne.getLb());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date,up,lo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.bp_date);
            up=itemView.findViewById(R.id.bp_upper);
            lo=itemView.findViewById(R.id.bp_lower);
        }
    }
}
