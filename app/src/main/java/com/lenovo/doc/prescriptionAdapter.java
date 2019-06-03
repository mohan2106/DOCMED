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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class prescriptionAdapter extends RecyclerView.Adapter<prescriptionAdapter.ViewHolder> {

    private List<prescription_class> itemList;
    private Context context;

    public prescriptionAdapter(List<prescription_class> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.single_priscription_layout,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final prescription_class ne=itemList.get(i);
        viewHolder.dr_name.setText(ne.getDr_name());
        viewHolder.dr_date.setText(ne.getDr_date());
        viewHolder.dr_reason.setText(ne.getDr_reason());
        final String img=ne.getDr_image();
        Glide.with(context).load(ne.getDr_image()).into(viewHolder.imageView);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ImageViewer.class);
                intent.putExtra("image",img);
                Toast.makeText(context, img, Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView dr_name,dr_date,dr_reason;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.single_image);
            dr_name=(TextView)itemView.findViewById(R.id.single_name);
            dr_date=(TextView)itemView.findViewById(R.id.single_date);
            dr_reason=(TextView)itemView.findViewById(R.id.single_disease);
        }
    }
}
