package com.krushna.veginew.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krushna.veginew.R;
import com.krushna.veginew.databinding.ItemTextviewBinding;
import com.krushna.veginew.models.AeraRoot;

import java.util.List;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {
    private List<AeraRoot.DataItem> areas;
    private OnAreaSelectListnear onAreaSelectListnear;

    public AreaAdapter(List<AeraRoot.DataItem> areas, OnAreaSelectListnear onAreaSelectListnear) {

        this.areas = areas;
        this.onAreaSelectListnear = onAreaSelectListnear;
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textview, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        holder.binding.text1.setText(areas.get(position).getName());
        holder.binding.text1.setOnClickListener(v -> onAreaSelectListnear.onAreaSelect(areas.get(position)));
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    public interface OnAreaSelectListnear {
        void onAreaSelect(AeraRoot.DataItem area);
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder {
        ItemTextviewBinding binding;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemTextviewBinding.bind(itemView);
        }
    }
}
