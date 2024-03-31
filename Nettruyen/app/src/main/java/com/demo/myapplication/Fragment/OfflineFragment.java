package com.demo.myapplication.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.myapplication.databinding.FragmentHomeBinding;
import com.demo.myapplication.databinding.FragmentLikeBinding;
import com.demo.myapplication.databinding.FragmentOfflineBinding;


public class OfflineFragment extends Fragment {
    private FragmentOfflineBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOfflineBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}