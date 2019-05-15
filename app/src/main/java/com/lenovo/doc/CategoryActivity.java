package com.lenovo.doc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<category_item_model> itemList;
    private category_adapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String city,local;
    private ProgressDialog dialog;
    private EditText editText;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Intent intent=getIntent();
        city=intent.getStringExtra("city");
        local=intent.getStringExtra("local");
        editText=(EditText)findViewById(R.id.search_text);
        dialog=new ProgressDialog(this);
        //firebaseFirestore=FirebaseFirestore.getInstance();
        //recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
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
    }
    private void filter(String text) {
        ArrayList<category_item_model> filteredList = new ArrayList<>();

        for (category_item_model item : itemList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }
    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this,2);
        adapter = new category_adapter(this,itemList,city,local);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }
    private void createExampleList(){
        itemList = new ArrayList<>();
        //dialog.setMessage("Loading Category");
        //dialog.show();
        Toast.makeText(this, "in category example funstion", Toast.LENGTH_SHORT).show();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("DoctorsCategory").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        itemList.add(new category_item_model(documentSnapshot.get("name").toString(),documentSnapshot.get("image").toString()));
                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    String error=task.getException().getMessage();
                    Toast.makeText(CategoryActivity.this, error, Toast.LENGTH_SHORT).show();
                }
                //   dialog.dismiss();
            }
        });

    }
}
