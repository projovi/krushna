package com.krushna.veginew.popups;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.krushna.veginew.R;
import com.krushna.veginew.databinding.PopupConfirmationBinding;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PaymentAleartPopup {
    Dialog dialog;

    public PaymentAleartPopup(Context context, String title, String decription, String amount, OnPopupClickListner onPopupClickListner, String positive, String negative) {
        dialog = new Dialog(context, R.style.customStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        PopupConfirmationBinding popupConfirmationBinding = DataBindingUtil.inflate(inflater, R.layout.popup_confirmation, null, false);
        popupConfirmationBinding.tvtitle.setText(title);
        if (decription.isEmpty()) {
            popupConfirmationBinding.tvDes.setVisibility(View.GONE);
        } else {
            popupConfirmationBinding.tvDes.setText(decription);
        }
        if (negative.isEmpty()) {
            popupConfirmationBinding.tvNagetive.setVisibility(View.GONE);
        }
        popupConfirmationBinding.tvPositive.setText(positive);
        popupConfirmationBinding.tvNagetive.setText(negative);

        if (!amount.isEmpty()) {
            popupConfirmationBinding.tvAmount.setVisibility(View.VISIBLE);
            popupConfirmationBinding.tvAmount.setText("Amount : " + amount);
        }
        dialog.setContentView(popupConfirmationBinding.getRoot());
        dialog.show();

        popupConfirmationBinding.tvPositive.setOnClickListener(v -> {
            dialog.dismiss();
            onPopupClickListner.onPositive();
        });
        popupConfirmationBinding.tvNagetive.setOnClickListener(v -> {
            dialog.dismiss();
            onPopupClickListner.onNegative();
        });


    }

    public interface OnPopupClickListner {
        void onPositive();

        void onNegative();
    }
}
