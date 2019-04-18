package com.lenovo.doc;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class searchActivity extends AppCompatActivity {
    private EditText editText;
    private ImageView search_btn;
    private ImageView img;
    private TextView tv1,tv2;
    private ArrayList<city_name> itemList;
    private RecyclerView mRecyclerView;
    private cityListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        img=(ImageView)findViewById(R.id.imageView7);
        tv1=(TextView)findViewById(R.id.textView3);
        tv2=(TextView)findViewById(R.id.textView9);
        editText=(EditText)findViewById(R.id.search_text);
        search_btn=(ImageView)findViewById(R.id.search_icon);
        createExampleList();
        buildRecyclerView();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        //lvToolbarSerch=(ListView) findViewById(R.id.list_view_search);

    }
    private void filter(String text) {
        ArrayList<city_name> filteredList = new ArrayList<>();
        img.setVisibility(View.INVISIBLE);
        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);

        for (city_name item : itemList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
        if(filteredList.size()==0){
            img.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
        }
    }
    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        adapter = new cityListAdapter(this,itemList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }
    private void createExampleList(){
        itemList = new ArrayList<>();
        itemList.add(new city_name("Jaipur"));
        itemList.add(new city_name("Patna"));
        itemList.add(new city_name("lucknow"));
        itemList.add(new city_name("Delhi"));
        itemList.add(new city_name("Mumbai"));
        itemList.add(new city_name("Bombay"));
        itemList.add(new city_name("Kota"));
        itemList.add(new city_name("Udaipur"));
        itemList.add(new city_name("Amritsar"));
        itemList.add(new city_name("Pune"));
        itemList.add(new city_name("Chennai"));
        itemList.add(new city_name("Hyderabad"));
        itemList.add(new city_name("Bengaluru"));
        itemList.add(new city_name("Kolkata"));
        itemList.add(new city_name("Guwahati"));
        itemList.add(new city_name("Dispur"));
        itemList.add(new city_name("Ahmedabad"));
        itemList.add(new city_name("Shilong"));


    }

}
