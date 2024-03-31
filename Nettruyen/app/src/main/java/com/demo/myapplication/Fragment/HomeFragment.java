package com.demo.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.myapplication.Activity.CreateTruyen;
import com.demo.myapplication.Activity.LoginActivity;
import com.demo.myapplication.Activity.MainActivity;
import com.demo.myapplication.Activity.Search;
import com.demo.myapplication.adapter.TrangChinhAdapter;
import com.demo.myapplication.databinding.ActivityForgotPasswordBinding;
import com.demo.myapplication.databinding.FragmentHomeBinding;
import com.demo.myapplication.model.TruyenTranh;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    TextView rank, aventure, fantasy, manhwa;
    RecyclerView toptruyen, theoAvent, theoFant, theoManw;

    String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        rank = binding.Rank;
        aventure = binding.Aventure;
        fantasy = binding.Fantasy;
        manhwa = binding.Manhwa;


        toptruyen = binding.TopTruyen;
        theoAvent = binding.theoAvent;
        theoFant = binding.theoFant;
        theoManw = binding.theoManw;

        loadTruyen();
        loadTruyenAdventure("Adventure");
        loadTruyenFantasy("Fantasy");
        loadTruyenManhwa("Manhwa");

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), Search.class);
                startActivity(intent);

            }
        });
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void loadTruyen(){
        //StorageReference storageReference = FirebaseStorage.getInstance().getReference("TruyenTranh/");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TruyenTranh");
        ArrayList<TruyenTranh> dataList = new ArrayList<>();
        TrangChinhAdapter adapter = new TrangChinhAdapter(getActivity(), dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        toptruyen.setLayoutManager(layoutManager);
        toptruyen.setAdapter(adapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

    }

    public void loadTruyenAdventure(String thing){
        //StorageReference storageReference = FirebaseStorage.getInstance().getReference("TruyenTranh/");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TruyenTranh");
        ArrayList<TruyenTranh> dataList = new ArrayList<>();
        TrangChinhAdapter adapter = new TrangChinhAdapter(getActivity(), dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        theoAvent.setLayoutManager(layoutManager);
        theoAvent.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TruyenTranh dataClass = dataSnapshot.getValue(TruyenTranh.class);
                    name = dataClass.getName();

                    GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>(){};
                    ArrayList<String> category = dataSnapshot.child("category").getValue(typeIndicator);

                    if(category!=null){
                        for(int i = 0; i<category.size(); i++){
                            if(category.get(i).equals(thing)){
                                dataList.add(dataClass);
                            }
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

    public void loadTruyenFantasy(String thing){
        //StorageReference storageReference = FirebaseStorage.getInstance().getReference("TruyenTranh/");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TruyenTranh");
        ArrayList<TruyenTranh> dataList = new ArrayList<>();
        TrangChinhAdapter adapter = new TrangChinhAdapter(getActivity(), dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        theoFant.setLayoutManager(layoutManager);
        theoFant.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TruyenTranh dataClass = dataSnapshot.getValue(TruyenTranh.class);
                    name = dataClass.getName();

                    GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>(){};
                    ArrayList<String> category = dataSnapshot.child("category").getValue(typeIndicator);

                    if(category!=null){
                        for(int i = 0; i<category.size(); i++){
                            if(category.get(i).equals(thing)){
                                dataList.add(dataClass);
                            }
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

    public void loadTruyenManhwa(String thing){
        //StorageReference storageReference = FirebaseStorage.getInstance().getReference("TruyenTranh/");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TruyenTranh");
        ArrayList<TruyenTranh> dataList = new ArrayList<>();
        TrangChinhAdapter adapter = new TrangChinhAdapter(getActivity(), dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        theoManw.setLayoutManager(layoutManager);
        theoManw.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TruyenTranh dataClass = dataSnapshot.getValue(TruyenTranh.class);
                    name = dataClass.getName();

                    GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>(){};
                    ArrayList<String> category = dataSnapshot.child("category").getValue(typeIndicator);

                    if(category!=null){
                        for(int i = 0; i<category.size(); i++){
                            if(category.get(i).equals(thing)){
                                dataList.add(dataClass);
                            }
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


}