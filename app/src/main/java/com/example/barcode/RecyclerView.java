package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView extends AppCompatActivity {
    List<String> list=new ArrayList<String>();
    androidx.recyclerview.widget.RecyclerView mrecyclerView;
    LinearLayoutManager layoutManager;   //layout Manager

    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        Intent intent = getIntent();
        String[] array = intent.getStringArrayExtra("sqlData");

        for(String lang:array){
            list.add(lang);
        }
        initRecyclerView();


    }


    private void initRecyclerView() {

        mrecyclerView=findViewById(R.id.RecyclerView);

        layoutManager = new LinearLayoutManager(this);

        layoutManager.setOrientation(androidx.recyclerview.widget.RecyclerView.VERTICAL); //Vertical is used to generate the message like items
        // Horizontal is used to generate pages like we scroll from messages to status in whatsapp.

        mrecyclerView.setLayoutManager(layoutManager); //Layout manager gives control to recycler view

        adapter=new Adapter(list);

        mrecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }
}