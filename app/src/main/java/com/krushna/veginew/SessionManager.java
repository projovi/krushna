package com.krushna.veginew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krushna.veginew.models.ProductItem;
import com.krushna.veginew.models.SettingRoot;
import com.krushna.veginew.models.UserRoot;
import com.krushna.veginew.retrofit.Const;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager {
    private static final String TAG = "sessonm";
    public static String userToken = "";
    public static String USER_ID = "";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.pref = context.getSharedPreferences(Const.PREF_NAME, MODE_PRIVATE);
        this.editor = this.pref.edit();
    }

    public void saveStringValue(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        return pref.getString(key, "");
    }

    public void saveBooleanValue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanValue(String key) {
        return pref.getBoolean(key, false);
    }

    public void saveUser(UserRoot.User user) {
        if (user != null) {
            USER_ID = String.valueOf(user.getId());

        }
        editor.putString(Const.USER, new Gson().toJson(user));
        editor.apply();
    }

    public UserRoot.User getUser() {
        String userString = pref.getString(Const.USER, "");
        if (!userString.isEmpty()) {
            return new Gson().fromJson(userString, UserRoot.User.class);
        }
        return null;
    }

    public void saveSetting(SettingRoot.Data setting) {
        editor.putString(Const.SETTING, new Gson().toJson(setting));
        editor.apply();
    }

    public SettingRoot.Data getSetting() {
        String userString = pref.getString(Const.SETTING, "");
        if (!userString.isEmpty()) {
            return new Gson().fromJson(userString, SettingRoot.Data.class);
        }
        return null;
    }

    public void toggleWishlist(ProductItem productItem) {
        List<ProductItem> wishlist = getWishlist();
        Log.d(TAG, "toggleWishlist: array size " + wishlist.size());

      /*  if (wishlist.contains(productItem)) {
            wishlist.remove(productItem);
            Log.d(TAG, "toggleWishlist: remove");
        } else {
            wishlist.add(productItem);
            Log.d(TAG, "toggleWishlist: add");
        }*/
        boolean flag = false;
        for (int i = 0; i < wishlist.size(); i++) {
            if (wishlist.get(i).getId() == productItem.getId()) {
                wishlist.remove(i);
                Log.d(TAG, "toggleWishlist: remove");
                flag = true;
            }
        }
        Log.d(TAG, "toggleWishlist: " + flag);
        if (!flag) {
            wishlist.add(productItem);
        }


        editor.putString(Const.WISHLIST, new Gson().toJson(wishlist));
        editor.apply();
    }

    public List<ProductItem> getWishlist() {
        String strData = pref.getString(Const.WISHLIST, "");
        if (!strData.isEmpty()) {
            return new Gson().fromJson(strData, new TypeToken<List<ProductItem>>() {
            }.getType());
        }
        return new ArrayList<>();
    }

}
