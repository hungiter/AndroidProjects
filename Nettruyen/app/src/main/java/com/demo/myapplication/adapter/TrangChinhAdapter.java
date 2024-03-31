package com.demo.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.myapplication.Activity.ReadComic;
import com.demo.myapplication.Activity.TruyenChiTiet;
import com.demo.myapplication.R;
import com.demo.myapplication.model.TruyenTranh;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TrangChinhAdapter extends RecyclerView.Adapter<TrangChinhAdapter.MyViewHolder>{
    private ArrayList<TruyenTranh> truyenTranhs;
    private Context context;
    private String text = "chap ";
    public TrangChinhAdapter(Context context, ArrayList<TruyenTranh> truyenTranhs) {
        this.context = context;
        this.truyenTranhs = truyenTranhs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trang_chinh, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("TruyenTranh/" + truyenTranhs.get(position).getName() + "/" + truyenTranhs.get(position).getName() + ".jpg");
        //Glide.with(context).load(truyenTranhs.get(position).getName()).into(holder.recyclerImage);

        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            holder.recyclerImage.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.ten.setText(truyenTranhs.get(position).getName());
        holder.chap.setText(String.format("%s%d", text, truyenTranhs.get(position).getChap()));
        TruyenTranh item = truyenTranhs.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TruyenChiTiet.class);

                intent.putExtra("TENTRUYEN", item.getName());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return truyenTranhs.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView recyclerImage;
        TextView ten,chap;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerImage = itemView.findViewById(R.id.item);
            ten = itemView.findViewById(R.id.tenTruyen);
            chap = itemView.findViewById(R.id.chapCuoi);
        }
    }
}
