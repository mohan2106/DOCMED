package com.lenovo.doc;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class cityListAdapter extends RecyclerView.Adapter<cityListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<city_name> cityArray;
    private String flag;
    private String local,city;

    public cityListAdapter(Context context, ArrayList<city_name> cityList,String flag,String local,String City) {
        this.context = context;
        this.cityArray=cityList;
        this.flag=flag;
        this.local=local;
        this.city=City;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(context).inflate(R.layout.single_city_name,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        city_name ne=cityArray.get(i);
        final String name=ne.getName();
        viewHolder.city.setText(ne.getName());
        viewHolder.city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,BookAppointment.class);
                intent.putExtra("City_name",name);
                intent.putExtra("flag",flag);
                intent.putExtra("category","Category");
                if(Integer.valueOf(flag)==0){
                    intent.putExtra("local",local);
                }
                else{
                    intent.putExtra("local",city);
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityArray.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView city;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            city=(TextView)itemView.findViewById(R.id.city_text);
        }
    }
    public void filterList(ArrayList<city_name> filteredList) {
        cityArray = filteredList;
        notifyDataSetChanged();
    }
}
