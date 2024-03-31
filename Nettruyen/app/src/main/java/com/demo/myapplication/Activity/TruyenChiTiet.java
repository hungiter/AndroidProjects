package com.demo.myapplication.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.myapplication.R;
import com.demo.myapplication.databinding.ActivityThemChuongBinding;
import com.demo.myapplication.databinding.ActivityTruyenChiTietBinding;
import com.demo.myapplication.model.AppRepo;
import com.demo.myapplication.model.Chap;
import com.demo.myapplication.model.ComicChapList;
import com.demo.myapplication.model.DataTheoDoi;
import com.demo.myapplication.model.TruyenTranh;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TruyenChiTiet extends AppCompatActivity {
    TextView comic, auth, categ, chap, detail;
    ImageView img;
    ListView chaplist;
    Button btn;

    String TenTruyen = "DR STONE";
    String cat="";
    int Chapter;
    String username = AppRepo.UserAccess.getUsername();
    DataTheoDoi dataTheoDoi = new DataTheoDoi(username);

    private ActivityTruyenChiTietBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTruyenChiTietBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);
        Intent itent = getIntent();
        TenTruyen = itent.getStringExtra("TENTRUYEN");

        Log.d("TENNGUOIDUNG", username);
        Log.d("TENTRUYEN", TenTruyen);

        initToolbar();
        binding.mdToolbarTitle.setText(TenTruyen);

        init();
        takeTruyen();
        toData();
        CheckTheoDoi();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = btn.getText().toString();
                setButton(buttonText);
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

    public void init(){
        comic = findViewById(R.id.ComicsName);
        auth = findViewById(R.id.AuthorName);
        categ = findViewById(R.id.CategoryName);
        chap = findViewById(R.id.NumChapter);
        detail = findViewById(R.id.detail);
        img = findViewById(R.id.Image);
        chaplist = findViewById(R.id.chaplist);
        btn = (Button)findViewById(R.id.theoDoi);

    }

    public void takeTruyen(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TruyenTranh");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("TruyenTranh/" + TenTruyen + "/" + TenTruyen + ".jpg");

        Query checkTruyen = reference.orderByChild("name").equalTo(TenTruyen);
        checkTruyen.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //TruyenTranh truyenTranh = snapshot.child(TenTruyen).child("name").getValue(TruyenTranh.class);
                    GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>(){};

                    TruyenTranh truyenTranh = snapshot.child(TenTruyen).getValue(TruyenTranh.class);
                    ArrayList<String> cate = snapshot.child(TenTruyen).child("category").getValue(typeIndicator);

                    Chapter = truyenTranh.getChap();
                    if (cate == null){
                        cat = "";
                    }else{
                        for(int i = 0; i < cate.size(); i++){
                            cat = cat +" " + cate.get(i).toString();
                        }
                    }


                    comic.setText(truyenTranh.getName());
                    chap.setText(""+truyenTranh.getChap());
                    detail.setText(truyenTranh.getDetail());
                    auth.setText(truyenTranh.getAuthor());
                    categ.setText(cat);
                    makeChapList();
                    //toData();
                    try {
                        File localfile = File.createTempFile("tempfile", ".jpg");
                        storageReference.getFile(localfile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                        img.setImageBitmap(bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TruyenChiTiet.this, "Không Lấy Được Ảnh", Toast.LENGTH_SHORT);
                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //kệ đoạn này
                    /*Intent intent = new Intent(TruyenChiTiet.this, ThemChuong.class);
                    intent.putExtra("name", truyenTranh.getName());
                    intent.putExtra("chapter", "chap "+truyenTranh.getChap());*/
                }else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void makeChapList(){
        ArrayList<String> data = new ArrayList();
        int a;
        for(int i = 0; i<Chapter; i++){
            a=i+1;
            data.add("chap " + a);
        }

        ArrayAdapter adapter = new ArrayAdapter<>(
                this,
                R.layout.my_list_chapter,
                R.id.chapter,
                data
        );


        chaplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(TruyenChiTiet.this, ReadComic.class);

                intent.putExtra("TENTRUYEN", TenTruyen);
                intent.putExtra("CHAP", data.get(position));
                startActivity(intent);
            }
        });

        chaplist.setAdapter(adapter);
    }

    public void CheckTheoDoi(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TheoDoi");
        Query checkTheoDoi = reference.orderByChild("user").equalTo(username);
        checkTheoDoi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>(){};
                    ArrayList<String> truyenTheoDoi = snapshot.child(username).child("truyen").getValue(typeIndicator);
                    //DataTheoDoi data = snapshot.child(username).getValue(DataTheoDoi.class);

                    btn.setText("THEO DÕI");
                    btn.setBackgroundColor(0xFF29CABA);

                    if(truyenTheoDoi==null){
                    }else {
                        if(truyenTheoDoi.contains(TenTruyen)){
                            btn.setText("BỎ THEO DÕI");
                            btn.setBackgroundColor(0xFFFF0000);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setButton(String text){
        if (text.equals("BỎ THEO DÕI")){
            if (dataTheoDoi.getTruyen()!=null){
                dataTheoDoi.removeTruyen(TenTruyen);
            }

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TheoDoi");
            reference.child(dataTheoDoi.getUser()).setValue(this.dataTheoDoi);
            Toast.makeText(TruyenChiTiet.this,"Bạn Đã Bỏ Theo Dõi",Toast.LENGTH_SHORT).show();

            btn.setText("THEO DÕI");
            btn.setBackgroundColor(0xFF29CABA);
        }else {
            dataTheoDoi.addTruyen(TenTruyen);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TheoDoi");
            reference.child(dataTheoDoi.getUser()).setValue(this.dataTheoDoi);

            Toast.makeText(TruyenChiTiet.this,"Bạn Đã Theo Dõi",Toast.LENGTH_SHORT).show();

            btn.setText("BỎ THEO DÕI");
            btn.setBackgroundColor(0xFFFF0000);
        }
    }

    public void toData(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TheoDoi");
        Query checkTheoDoi = reference.orderByChild("user").equalTo(username);

        checkTheoDoi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>(){};
                    ArrayList<String> truyenTheoDoi = snapshot.child(username).child("truyen").getValue(typeIndicator);
                    if(truyenTheoDoi!=null){
                        dataTheoDoi.setTruyen(truyenTheoDoi);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}