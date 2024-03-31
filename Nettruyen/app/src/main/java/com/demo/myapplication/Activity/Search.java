package com.demo.myapplication.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.myapplication.R;
import com.demo.myapplication.adapter.SearchAdapter;
import com.demo.myapplication.databinding.ActivitySearchBinding;
import com.demo.myapplication.databinding.ActivityTruyenChiTietBinding;
import com.demo.myapplication.model.TruyenTranh;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    SearchView searchView;
    SearchAdapter adapter;
    RecyclerView recyclerView;

    DatabaseReference databaseReference;
    ArrayList<TruyenTranh> dataList;

    private ActivitySearchBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);
        initToolbar();

        searchView = findViewById(R.id.search);
        searchView.clearFocus();
        recyclerView = findViewById(R.id.recyclerView);

        databaseReference = FirebaseDatabase.getInstance().getReference("TruyenTranh");
        dataList = new ArrayList<>();

        adapter = new SearchAdapter(this, dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TruyenTranh dataClass = dataSnapshot.getValue(TruyenTranh.class);
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
    }
    private void initToolbar() {
        setSupportActionBar(binding.mdToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        binding.mdToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

/*    public void loadTruyen(){
        databaseReference = FirebaseDatabase.getInstance().getReference("TruyenTranh");
        dataList = new ArrayList<>();

        SearchAdapter adapter = new SearchAdapter(this, dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TruyenTranh dataClass = dataSnapshot.getValue(TruyenTranh.class);
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }*/

    public void searchList(String text){
        ArrayList<TruyenTranh> searchList = new ArrayList<>();
        /*for (TruyenTranh dataClass: dataList){
            if (dataClass.getName().toUpperCase().contains(text.toUpperCase())){
                searchList.add(dataClass);
            }
        }*/
        for (int i = 0; i<dataList.size(); i++){
            if(dataList.get(i).getName().toUpperCase().contains(text.toUpperCase())){
                searchList.add(dataList.get(i));
            }
        }
        if(searchList!=null){
            adapter.searchDataList(searchList);
        }

    }
}