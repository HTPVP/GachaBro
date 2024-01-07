package com.example.gachabro.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gachabro.R;
import com.example.gachabro.ViewHolder.MyViewHolder;
import com.example.gachabro.model.Character_items;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class search_bar_adapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<Character_items> items;

    public search_bar_adapter(Context context, List<Character_items> items) {
        this.context = context;
        this.items = items;

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

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Character_items> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    // Return all items if no constraint is given
                    filteredList.addAll(items);
                } else {
                    // Filter items based on the constraint
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Character_items item : items) {
                        if (item.getPrefix().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                Filter.FilterResults results = new Filter.FilterResults();
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                // Update the data in the adapter
                items.clear();
                items.addAll((Collection<? extends Character_items>) results.values);
                notifyDataSetChanged();
            }
        };
    }

}
