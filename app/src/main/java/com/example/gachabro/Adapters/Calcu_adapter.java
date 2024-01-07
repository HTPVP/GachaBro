package com.example.gachabro.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gachabro.R;
import com.example.gachabro.ViewHolder.MyViewHolder;
import com.example.gachabro.model.Character_items;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.LogRecord;

public class Calcu_adapter extends RecyclerView.Adapter<MyViewHolder> {


    Context context;
    List<Character_items> items;
    private View.OnClickListener add_char;
    private View.OnClickListener add_wpn;

    public interface add_char{
        void onClick(int position);
    }

    public interface add_wpn{
        void onClick(int position);
    }

    public void setAdd_char(View.OnClickListener add_char) {
        this.add_char = add_char;
    }

    public void setAdd_wpn(View.OnClickListener add_wpn) {
        this.add_wpn = add_wpn;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.calculator_fragment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(add_char != null){
            holder.itemView.setOnClickListener(add_char);


        }
        if(add_wpn != null){
            holder.itemView.setOnClickListener(add_wpn);
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    private class ItemFilter extends Filter {
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

            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Update the data in the adapter
            items.clear();
            items.addAll((Collection<? extends Character_items>) results.values);
            notifyDataSetChanged();
        }
    }

    public Filter getFilter() {
        return new ItemFilter();
    }
}
