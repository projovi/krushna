package com.krushna.veginew.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.krushna.veginew.R;
import com.krushna.veginew.VegiUtils;
import com.krushna.veginew.activites.AddAddressActivity;
import com.krushna.veginew.databinding.ItemAddressBinding;
import com.krushna.veginew.models.DeliveryAddress;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    OnClickDeleveryAddress onClickDeleveryAddress;
    private List<DeliveryAddress> data = new ArrayList<>();

    public OnClickDeleveryAddress getOnClickDeleveryAddress() {
        return onClickDeleveryAddress;
    }

    private Context context;

    public void setOnClickDeleveryAddress(OnClickDeleveryAddress onClickDeleveryAddress) {
        this.onClickDeleveryAddress = onClickDeleveryAddress;
    }

    public void delete(DeliveryAddress datum) {
        data.remove(datum);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        holder.setAddress(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    public void addData(List<DeliveryAddress> data) {

        this.data = data;
        notifyDataSetChanged();
    }

    public interface OnClickDeleveryAddress {
        void onDeleteClick(DeliveryAddress datum);
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        ItemAddressBinding binding;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAddressBinding.bind(itemView);
        }

        public void setAddress(int position) {
            DeliveryAddress address = data.get(position);
            binding.tvName.setText(address.getFirstname().concat(" "));
            binding.tvAddress.setText(address.getAddress().concat(" " +
                    address.getArea().getName() + " " +
                    address.getCity().getName() + " " +
                    address.getPincode()));
            binding.tvMobile.setText(address.getNumber().concat("  " +
                    (address.getAltNumber() != null ? address.getAltNumber() : "")));

            if (address.getIsDefault() == 1) {
                binding.tvDefult.setVisibility(View.VISIBLE);
            } else {
                binding.tvDefult.setVisibility(View.GONE);
            }
            String s = VegiUtils.getAddressType(address.getAddressType());
            binding.tvType.setText(s);
            binding.imgEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddAddressActivity.class);
                intent.putExtra("address", new Gson().toJson(address));
                context.startActivity(intent);
            });
            binding.imgDelete.setOnClickListener(v -> onClickDeleveryAddress.onDeleteClick(address));

        }
    }
}
