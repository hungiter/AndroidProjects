package com.demo.myapplication.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.myapplication.Activity.AddNewChapter;
import com.demo.myapplication.Activity.ChangePasswordActivity;
import com.demo.myapplication.Activity.CreateTruyen;
import com.demo.myapplication.Activity.LoginActivity;
import com.demo.myapplication.Activity.PolicyActivity;
import com.demo.myapplication.Activity.ReportActivity;
import com.demo.myapplication.Activity.UserActivity;
import com.demo.myapplication.R;
import com.demo.myapplication.databinding.FragmentLikeBinding;
import com.demo.myapplication.databinding.FragmentOfflineBinding;
import com.demo.myapplication.databinding.FragmentSettingsBinding;
import com.demo.myapplication.model.AppRepo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;


public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        if(AppRepo.UserAccess.getAccess().equals("none")){
            binding.btnCreateTruyen.setVisibility(View.GONE);
            binding.btnAddChapter.setVisibility(View.GONE);

        }
        initLayout();
        initObserve();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void initLayout(){
        long cacheSize = 0;
        try {
            File cacheDir =  getContext().getCacheDir();
            if (cacheDir != null) {
                File[] cacheFiles = cacheDir.listFiles();
                if (cacheFiles != null) {
                    for (File cacheFile : cacheFiles) {
                        cacheSize += cacheFile.length();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String formattedCacheSize =  Formatter.formatFileSize(getContext(), cacheSize);
        binding.txtCache.setText(formattedCacheSize);

    }

    private void initObserve(){
        binding.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserActivity.class);
                startActivity(intent);
            }
        });
        binding.btnPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PolicyActivity.class);
                startActivity(intent);
            }
        });
        binding.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                startActivity(intent);
            }
        });
        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                AppRepo.resetDefault();
                Toast.makeText(getActivity(), "Đã thoát!", Toast.LENGTH_LONG).show();
            }
        });
        binding.btnClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(getActivity(), R.style.Custom_ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                        .setTitle("Thông báo")
                        .setMessage("Xác nhận xóa cache!")
                        .setNeutralButton("Thoát", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File dir = getContext().getCacheDir();
                                if (dir != null && dir.isDirectory()) {
                                    deleteDir(dir);
                                }
                                initLayout();
                                Toast.makeText(getActivity(), "Đã Xóa Cache!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        binding.btnCreateTruyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateTruyen.class);
                startActivity(intent);
            }
        });
        binding.btnAddChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNewChapter.class);
                startActivity(intent);
            }
        });
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else {
            return dir != null && dir.isFile() && dir.delete();
        }
    }
}