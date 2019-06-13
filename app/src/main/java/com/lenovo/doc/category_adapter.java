package com.lenovo.doc;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class category_adapter extends RecyclerView.Adapter<category_adapter.VIEWHOLDER> {
    private Context context;
    private ArrayList<category_item_model> itemList;
    private String city;
    private String local;

    public category_adapter(Context context, ArrayList<category_item_model> itemList, String city, String local) {
        this.context = context;
        this.itemList = itemList;
        this.city = city;
        this.local = local;
    }

    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(context).inflate(R.layout.category_single_layout,viewGroup,false);
        return new VIEWHOLDER(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VIEWHOLDER viewholder, int i) {
        category_item_model ne=itemList.get(i);
        viewholder.name.setText(ne.getName());
        Glide.with(context)
                .load(ne.getImage())
                .into(viewholder.image);
        final String name=ne.getName();
        viewholder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,BookAppointment.class);
                intent.putExtra("flag","0");
                intent.putExtra("city_name",city);
                intent.putExtra("local",local);
                intent.putExtra("category",name);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity)CategoryActivity.category_activity).toBundle());
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

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView image;
        private LinearLayout layout;
        public VIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.cate_text);
            image=(ImageView) itemView.findViewById(R.id.cate_image);
            layout=(LinearLayout)itemView.findViewById(R.id.category_layout);
        }
    }
    public void filterList(ArrayList<category_item_model> filteredList) {
        itemList = filteredList;
        notifyDataSetChanged();
    }
}
