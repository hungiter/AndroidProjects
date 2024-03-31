package com.demo.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.demo.myapplication.Fragment.HomeFragment;
import com.demo.myapplication.databinding.ActivityLoginBinding;
import com.demo.myapplication.model.AppRepo;
import com.demo.myapplication.model.HelpData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);
        initObserve();
    }

    private void initObserve(){
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        binding.btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validUsername() || !validPassword()){

                }else{
                    checkUser();
                }
            }
        });
    }

    public boolean validUsername(){
        String val = binding.tfUsername.getEditText().getText().toString();

        if(val.isEmpty()){
            binding.tfUsername.setError("Bạn Không thể Để Trống");
            return false;
        }else{
            binding.tfUsername.setError(null);
            return true;
        }
    }

    public boolean validPassword(){
        String val = binding.tfPassword.getEditText().getText().toString();

        if(val.isEmpty()){
            binding.tfPassword.setError("Bạn Không thể Để Trống");
            return false;
        }else{
            binding.tfPassword.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String Username = binding.tfUsername.getEditText().getText().toString();
        String Userpass = binding.tfPassword.getEditText().getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Query checkUserdb = reference.orderByChild("username").equalTo(Username);

        checkUserdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.tfUsername.setError(null);
                    String passwordFromDB = snapshot.child(Username).child("pass").getValue(String.class);
                    if (passwordFromDB.equals(Userpass)) {
                        binding.tfUsername.getEditText().setError(null);
                        String emailFromDB = snapshot.child(Username).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(Username).child("username").getValue(String.class);

                        String accessFromDB = snapshot.child(Username).child("access").getValue(String.class);

                        AppRepo.UserAccess = new HelpData(emailFromDB,usernameFromDB,passwordFromDB,accessFromDB);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("pass", passwordFromDB);

                        //Fragment's intent
                        startActivity(intent);
                    } else {
                        binding.tfPassword.setError("Invalid Credentials");
                        binding.tfPassword.requestFocus();
                    }
                } else {
                    binding.tfUsername.setError("User does not exist");
                    binding.tfUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
