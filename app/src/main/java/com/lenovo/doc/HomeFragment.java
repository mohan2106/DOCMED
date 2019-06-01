package com.lenovo.doc;


import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private LinearLayout book_appoint,buy_med,call_us,book;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        book=(LinearLayout) v.findViewById(R.id.health_book);
        book_appoint=(LinearLayout)v.findViewById(R.id.book_appointment);
        buy_med=(LinearLayout)v.findViewById(R.id.buy_medicine);
        call_us=(LinearLayout)v.findViewById(R.id.call_us_for_booking);
        book_appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),CategoryActivity.class);
                /*intent.putExtra("City_name","Guwahati");
                intent.putExtra("local","Locality");
                intent.putExtra("flag","0");
                intent.putExtra("category","Category");*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }
                else{
                    startActivity(intent);
                }
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(new Intent(container.getContext(),HealthBookHome.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }
                else{
                    startActivity(new Intent(container.getContext(),HealthBookHome.class));
                }
            }
        });
        buy_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(),order_medicine_activity.class));
            }
        });
        call_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telNumber="9876543210";
                final String data="tel:"+telNumber;
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(data));
                //startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }
                else{
                    startActivity(intent);
                }
            }
        });
        return v;
    }

}
