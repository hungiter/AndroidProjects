package com.demo.myapplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.myapplication.R;
import com.demo.myapplication.adapter.ComicAdapter;
import com.demo.myapplication.databinding.ActivityReadComicBinding;
import com.demo.myapplication.databinding.ActivityTruyenChiTietBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ReadComic extends AppCompatActivity {

    String chap = "chap 1";
    String truyen = "DR STONE";
    int tongchap = 5;
    int a;
    int index;
    String[] chapter = new String[tongchap];

    private RecyclerView recyclerView;
    private ArrayList<String> dataList;
    private ComicAdapter adapter;
    Button pre,next;
    private ActivityReadComicBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadComicBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);
        initToolbar();

        Intent itent = getIntent();
        chap = itent.getStringExtra("CHAP");
        truyen = itent.getStringExtra("TENTRUYEN");

        binding.mdToolbarTitle.setText(chap);

        pre = findViewById(R.id.prebtn);
        next = findViewById(R.id.nextbtn);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for(int i = 0; i<tongchap; i++){
            a=a+1;
            chapter[i] = "chap " + a;
        }

        for(int i = 0; i<tongchap; i++){
            if(chapter[i].equals(chap)){
                index = i;
            }
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = index + 1;
                if(index < tongchap){
                    next.setEnabled(true);
                    pre.setEnabled(true);
                    chap = chapter[index];
                    //dataList.clear();
                    binding.mdToolbarTitle.setText(chap);

                    loadTruyen();
                }
                else{
                    next.setEnabled(false);
                    Toast.makeText(ReadComic.this, "Đã Là chap mới nhất", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = index - 1;
                if(index >= 0 && index < tongchap){
                    next.setEnabled(true);
                    pre.setEnabled(true);
                    chap = chapter[index];
                    //dataList.clear();
                    binding.mdToolbarTitle.setText(chap);
                    loadTruyen();
                }else{
                    pre.setEnabled(false);
                    Toast.makeText(ReadComic.this, "Đã lùi về chap đầu tiên", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadTruyen();

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


    public void loadTruyen(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("TruyenTranh/" + truyen + "/" + chap);
        dataList = new ArrayList<>();
        adapter = new ComicAdapter(this, dataList);
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference file:listResult.getItems()){
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dataList.add(uri.toString());
                            Log.e("Itemvalue",uri.toString());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        });
    }
}