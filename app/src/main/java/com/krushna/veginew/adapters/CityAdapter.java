package com.krushna.veginew.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krushna.veginew.R;
import com.krushna.veginew.databinding.ItemTextviewBinding;
import com.krushna.veginew.models.CityRoot;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private List<CityRoot.DataItem> cites;
    private OnCitiySelectListnear onCitiySelectListnear;

    public CityAdapter(List<CityRoot.DataItem> cites, OnCitiySelectListnear onCitiySelectListnear) {

        this.cites = cites;
        this.onCitiySelectListnear = onCitiySelectListnear;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textview, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        holder.binding.text1.setText(cites.get(position).getName());
        holder.binding.text1.setOnClickListener(v -> onCitiySelectListnear.onCitiyClick(cites.get(position)));
    }

    @Override
    public int getItemCount() {
        return cites.size();
    }

    public interface OnCitiySelectListnear {
        void onCitiyClick(CityRoot.DataItem city);
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {
        ItemTextviewBinding binding;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemTextviewBinding.bind(itemView);
        }
    }
}
