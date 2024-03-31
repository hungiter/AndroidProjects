package com.demo.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.demo.myapplication.R;
import com.demo.myapplication.databinding.ActivityChangePasswordBinding;
import com.demo.myapplication.databinding.ActivityReportBinding;
import com.demo.myapplication.databinding.ActivityUserBinding;

public class ReportActivity extends AppCompatActivity {
    private ActivityReportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);
        initToolbar();
        initObserve();
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
    private void initObserve(){
        binding.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ReportActivity.this, "Gửi báo cáo thành công!", Toast.LENGTH_LONG).show();
            }
        });
    }
}