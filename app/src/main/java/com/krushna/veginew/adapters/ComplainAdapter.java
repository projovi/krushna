package com.krushna.veginew.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krushna.veginew.R;
import com.krushna.veginew.databinding.ItemTicketcloseBinding;
import com.krushna.veginew.models.ComplainRoot;

import java.util.ArrayList;
import java.util.List;

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.ComplainViewHolder> {

    private Context context;
    private List<ComplainRoot.DataItem> data = new ArrayList<>();
    private int type;

    public ComplainAdapter(int type) {

        this.type = type;
    }


    @Override
    public void onBindViewHolder(@NonNull ComplainViewHolder holder, int position) {

        ComplainRoot.DataItem complain = data.get(position);
        holder.binding.tvtime.setText(complain.getCreatedAt());
        holder.binding.tvid.setText(complain.getComplaintsId());

        holder.binding.tvtitle.setText(complain.getTitle());
        holder.binding.tvdes.setText(complain.getDescription());
        if (type == 0) {
            holder.binding.reply.setVisibility(View.GONE);
            holder.binding.lytReply.setVisibility(View.GONE);
        } else {
            holder.binding.reply.setVisibility(View.VISIBLE);
            holder.binding.lytReply.setVisibility(View.VISIBLE);
            holder.binding.tvreply.setText(complain.getAnswer() != null ? complain.getAnswer() : "");
        }
    }

    @NonNull
    @Override
    public ComplainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticketclose, parent, false);
        return new ComplainViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public void addData(List<ComplainRoot.DataItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    public static class ComplainViewHolder extends RecyclerView.ViewHolder {
        ItemTicketcloseBinding binding;

        public ComplainViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemTicketcloseBinding.bind(itemView);
        }
    }
}
