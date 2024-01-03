package com.example.gachabro.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gachabro.R;
import com.example.gachabro.ViewHolder.MyViewHolder;
import com.example.gachabro.model.Character_items;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class search_bar_adapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<String> dataList = new ArrayList<>();

    Context context;
    List<Character_items> items;

    public search_bar_adapter(Context context, List<Character_items> items, List<String> dataList) {
        this.context = context;
        this.items = items;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.search_bar_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Character_items item = items.get(position);
        String imagePath = item.getImagePath();

        Bitmap bitmap = loadImageFromFile(imagePath);
        if (bitmap != null) {
            holder.imageView.setImageBitmap(bitmap);
        }

        String imageName = item.getPrefix();
        if(imageName != null){
            holder.textView.setText(imageName);
        }
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void updateData(List<String> newData) {
        this.dataList = newData;
        notifyDataSetChanged();
    }

    private Bitmap loadImageFromFile(String filename) {
        try {
            File directory = new File(context.getFilesDir(), "images");
            File imageFile = new File(directory, filename);
            return BitmapFactory.decodeStream(new FileInputStream(imageFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
