package com.demo.myapplication.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.myapplication.R;
import com.demo.myapplication.adapter.CategoryAdapter;
import com.demo.myapplication.databinding.ActivityCreateTruyenBinding;
import com.demo.myapplication.model.Category;
import com.demo.myapplication.model.TruyenTranh;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class CreateTruyen extends AppCompatActivity {
    public static final String TAG = "recyclerView";
    FirebaseDatabase database;
    DatabaseReference reference;

    private Uri imageUri;
    DatabaseReference referenceIMG = FirebaseDatabase.getInstance().getReference("TruyenTranh");

    RecyclerView recyclerView;
    Button btnTaoTruyen;
    EditText tentruyen, tacgia, mota;
    ImageView uploadImage;

    ArrayList<Category> categorydata =  new ArrayList<>();
    String[] data = {"Adventure", "Drama", "Ecchi", "Harem", "Historical", "Horror", "Manhwa", "Manhua", "Mecha",
            "Mystery", "Psychological", "Romance", "School Life", "Slice of Life", "Sports", "Tragedy", "Comedy", "Fantasy"};

    TruyenTranh newTruyen;

    private ActivityCreateTruyenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateTruyenBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);
        initToolbar();

        recyclerView = findViewById(R.id.Review);
        btnTaoTruyen = findViewById(R.id.ThemTruyen);
        tentruyen = findViewById(R.id.TenTruyen);
        tacgia = findViewById(R.id.TacGia);
        mota = findViewById(R.id.MoTa);
        uploadImage = findViewById(R.id.AnhTruyen);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            imageUri = data.getData();
                            uploadImage.setImageURI(imageUri);
                        } else {
                            Toast.makeText(CreateTruyen.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        CategoryAdapter categoryAdapter = new CategoryAdapter(this, getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(categoryAdapter);

        btnTaoTruyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null){
                    uploadToFirebase(imageUri);
                } else  {
                    Toast.makeText(CreateTruyen.this, "Please select image", Toast.LENGTH_SHORT).show();
                }

                if(!validtentruyen() || !validtacgia() || !validmota()){

                }else {
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("TruyenTranh");

                    String TenTruyen = tentruyen.getText().toString();
                    String TacGia = tacgia.getText().toString();
                    String MoTa = mota.getText().toString();

                    TruyenTranh helpData = new TruyenTranh(TenTruyen, TacGia, MoTa);
                    ArrayList<Category> cat = categoryAdapter.takeData();
                    for(int i = 0; i< cat.size(); i++){
                        helpData.setCategory(cat, i);
                    }helpData.setChapter();
                    reference.child(TenTruyen).setValue(helpData);

                    Toast.makeText(CreateTruyen.this, "Bạn Thêm Một Truyện Mới Thành Công", Toast.LENGTH_SHORT).show();
                }
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

    //Outside onCreate
    private void uploadToFirebase(Uri uri){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("TruyenTranh/" + tentruyen.getText().toString());
        final StorageReference imageReference = storageReference.child(tentruyen.getText().toString() + "." + getFileExtension(uri));
        imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //DataClass dataClass = new DataClass(uri.toString());
                        //referenceIMG.child(tentruyen.getText().toString()).setValue(dataClass);
                        Toast.makeText(CreateTruyen.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateTruyen.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

    private ArrayList<Category> getData(){
        for(int i =0; i<data.length; i++){
            categorydata.add(new Category(data[i]));
        }
        return categorydata;
    }

    public boolean validtentruyen(){
        String val = tentruyen.getText().toString();

        if(val.isEmpty()){
            tentruyen.setError("Bạn Không thể Để Trống");
            return false;
        }else{
            tentruyen.setError(null);
            return true;
        }
    }
    public boolean validtacgia(){
        String val = tacgia.getText().toString();

        if(val.isEmpty()){
            tacgia.setError("Bạn Không thể Để Trống");
            return false;
        }else{
            tacgia.setError(null);
            return true;
        }
    }
    public boolean validmota(){
        String val = mota.getText().toString();

        if(val.isEmpty()){
            mota.setError("Bạn Không thể Để Trống");
            return false;
        }else{
            mota.setError(null);
            return true;
        }
    }

}