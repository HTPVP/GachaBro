package com.example.gachabro.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gachabro.R;
import com.example.gachabro.ViewHolder.MyViewHolder;
import com.example.gachabro.model.Character_items;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Char_active_adapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<Character_items> items;

    public Char_active_adapter(Context context, List<Character_items> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.char_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Character_items item = items.get(position);
        String imagePath = item.getImagePath();
        Log.d("Debug1", "Image Path: " + imagePath);
        File imageFile = new File(context.getFilesDir(), "images/" + imagePath);
        Uri imageUri = Uri.fromFile(imageFile);

        Picasso.get().load(imageUri)
                .into(holder.imageView);

        String imageName = item.getPrefix();
        if(imageName != null){
            holder.textView.setText(imageName);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
