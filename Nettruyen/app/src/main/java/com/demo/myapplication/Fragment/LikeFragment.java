package com.demo.myapplication.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.myapplication.adapter.SearchAdapter;
import com.demo.myapplication.databinding.FragmentHomeBinding;
import com.demo.myapplication.databinding.FragmentLikeBinding;
import com.demo.myapplication.model.AppRepo;
import com.demo.myapplication.model.TruyenTranh;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LikeFragment extends Fragment {

    SearchAdapter adapter;
    RecyclerView recyclerView;

    DatabaseReference databaseReference;
    ArrayList<TruyenTranh> dataList;
    ArrayList<String> truyenTheoDoi;

    String username = "bak";
    private FragmentLikeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLikeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        username = AppRepo.UserAccess.getUsername();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        recyclerView = binding.TheoDoiView;
        databaseReference = FirebaseDatabase.getInstance().getReference("TruyenTranh");
        dataList = new ArrayList<>();

        adapter = new SearchAdapter(getActivity(), dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        takeTheoDoiData();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TruyenTranh dataClass = dataSnapshot.getValue(TruyenTranh.class);
                    if (truyenTheoDoi != null){
                        if(truyenTheoDoi.contains(dataClass.getName())){
                            dataList.add(dataClass);
                        }
                    }


                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void takeTheoDoiData(){
        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("TheoDoi");
        Query checkTheoDoi = Reference.orderByChild("user").equalTo(username);

        checkTheoDoi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>(){};
                    truyenTheoDoi = snapshot.child(username).child("truyen").getValue(typeIndicator);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}