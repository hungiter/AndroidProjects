package com.demo.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.demo.myapplication.R;
import com.demo.myapplication.databinding.ActivityChangePasswordBinding;
import com.demo.myapplication.databinding.ActivityUserBinding;
import com.demo.myapplication.model.AppRepo;
import com.demo.myapplication.model.HelpData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());

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
        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = "";
                String newPassword = "";
                if (TextUtils.isEmpty(binding.tfPassword.getEditText().getText())) {
                    Toast.makeText(ChangePasswordActivity.this, "Vui lòng nhập Mật khẩu cũ!", Toast.LENGTH_LONG).show();
                    return;
                }

                password = binding.tfPassword.getEditText().getText().toString();

                if(!password.equals(AppRepo.UserAccess.getPass())) {
                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu cũ không đúng!", Toast.LENGTH_LONG).show();
                }

                if (!binding.tfNewPassword.getEditText().getText().toString().equals(binding.tfReNewPassword.getEditText().getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu không trùng khớp!", Toast.LENGTH_LONG).show();
                    return;
                }

                newPassword = binding.tfNewPassword.getEditText().getText().toString();
                if(password==newPassword){
                    Toast.makeText(ChangePasswordActivity.this, "Không nên sử dụng mật khẩu cũ", Toast.LENGTH_LONG).show();
                    return;
                }

                updatePassword(newPassword);
            }
        });
    }

    private void updatePassword(String pass){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        HelpData helpData = new HelpData(AppRepo.UserAccess.getEmail(), AppRepo.UserAccess.getUsername(), pass);
        userRef.child(AppRepo.UserAccess.getUsername()).setValue(helpData);
        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_LONG).show();
        finish();
    }
}