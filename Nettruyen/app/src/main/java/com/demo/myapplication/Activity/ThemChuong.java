package com.demo.myapplication.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.myapplication.R;
import com.demo.myapplication.databinding.ActivityAddNewChapterBinding;
import com.demo.myapplication.databinding.ActivityCreateTruyenBinding;
import com.demo.myapplication.databinding.ActivityThemChuongBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ThemChuong extends AppCompatActivity {
    Button add, back;
    ImageView img;
    TextView test;

    FirebaseDatabase database;
    DatabaseReference reference;

    private Uri imageUri;
    String name, chapter;
    int number = 0;
    private ActivityThemChuongBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemChuongBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);
        initToolbar();

        add = findViewById(R.id.add);
        img = findViewById(R.id.img);
        test = findViewById(R.id.test);
        test.setText("Num 0");


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        chapter = intent.getStringExtra("chapter");

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageUri = data.getData();
                            img.setImageURI(imageUri);
                        } else {
                            Toast.makeText(ThemChuong.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                number += 1;
                if (imageUri != null) {
                    uploadToFirebase(imageUri);
                    String name = "num" + number;
                    test.setText(name);
                } else {
                    Toast.makeText(ThemChuong.this, "Please select image", Toast.LENGTH_SHORT).show();
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

    private void uploadToFirebase(Uri uri) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("TruyenTranh/" + name).child(chapter);

        final StorageReference imageReference = storageReference.child(number + "." + getFileExtension(uri));
        imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(ThemChuong.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ThemChuong.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri fileUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}