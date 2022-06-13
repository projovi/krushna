package com.krushna.veginew.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krushna.veginew.R;
import com.krushna.veginew.databinding.ItemFaqsBinding;
import com.krushna.veginew.models.FaqRoot;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqViewHolder> {


    private List<FaqRoot.DataItem> faqs;

    public FaqAdapter(List<FaqRoot.DataItem> faqs) {

        this.faqs = faqs;
    }

    @NonNull
    @Override
    public FaqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faqs, parent, false);
        return new FaqViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqViewHolder holder, int position) {

        holder.binding.tvquestion.setText(faqs.get(position).getQuestion());
        holder.binding.tvans.setText(faqs.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        return faqs.size();
    }

    public class FaqViewHolder extends RecyclerView.ViewHolder {
        ItemFaqsBinding binding;

        public FaqViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemFaqsBinding.bind(itemView);
        }
    }
}
