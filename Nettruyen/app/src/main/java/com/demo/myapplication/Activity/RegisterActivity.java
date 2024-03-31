package com.demo.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.demo.myapplication.databinding.ActivityLoginBinding;
import com.demo.myapplication.databinding.ActivityRegisterBinding;
import com.demo.myapplication.model.HelpData;
import com.demo.myapplication.model.IValidation;
import com.demo.myapplication.model.ValidGmail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initObserve();
        initToolbar();
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
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validEmail() || !validtaikhoan() || !validpass() || !validxacthucpass() || !Gmail()){

                }else {
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("Users");

                    String Username = binding.tfUsername.getEditText().getText().toString();
                    String Email = binding.tfEmail.getEditText().getText().toString();
                    String Password = binding.tfPassword.getEditText().getText().toString();

                    HelpData helpData = new HelpData(Email, Username, Password);
                    reference.child(Username).setValue(helpData);

                    Toast.makeText(RegisterActivity.this, "Bạn Tạo Tài Khoản Thành Công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean validEmail(){
        String val = binding.tfEmail.getEditText().getText().toString();

        if(val.isEmpty()){
            binding.tfEmail.setError("Bạn Không thể Để Trống");
            return false;
        }else{
            binding.tfEmail.setError(null);
            return true;
        }
    }

    public boolean Gmail(){
        IValidation valid = new ValidGmail();
        if(!valid.valid(binding.tfEmail.getEditText().getText().toString())){
            binding.tfEmail.setError("Đây không phải là Email");
            return false;
        }
        else{
            binding.tfEmail.setError(null);
            return true;
        }
    }
    public boolean validtaikhoan(){
        String val = binding.tfUsername.getEditText().getText().toString();

        if(val.isEmpty()){
            binding.tfUsername.setError("Bạn Không thể Để Trống");
            return false;
        }else{
            binding.tfUsername.setError(null);
            return true;
        }
    }
    public boolean validpass(){
        String val = binding.tfPassword.getEditText().getText().toString();

        if(val.isEmpty()){
            binding.tfPassword.setError("Bạn Không thể Để Trống");
            Toast.makeText(RegisterActivity.this, "Bạn Không thể Để Trống", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            binding.tfPassword.setError(null);
            return true;
        }
    }
    public boolean validxacthucpass(){
        String val = binding.tfRePassword.getEditText().getText().toString();
        String val2 = binding.tfPassword.getEditText().getText().toString();

        if(val.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Bạn Không thể Để Trống", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!binding.tfRePassword.getEditText().getText().toString().equals(binding.tfPassword.getEditText().getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Mật khẩu không giống nhau", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            binding.tfRePassword.setError(null);
            return true;
        }
    }



}