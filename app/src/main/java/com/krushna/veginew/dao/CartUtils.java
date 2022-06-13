package com.krushna.veginew.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.krushna.veginew.models.PricesItem;
import com.krushna.veginew.models.ProductItem;
import com.krushna.veginew.retrofit.Const;

public class CartUtils {
    private final AppDatabase db;
    private Context context;

    public CartUtils(Context context) {
        this.context = context;

        db = Room.databaseBuilder(context,
                AppDatabase.class, Const.DB_NAME).allowMainThreadQueries().build();

    }

    public void add(ProductItem product, PricesItem pricesItem) {
        String pid = String.valueOf(product.getId());
        String name = product.getName();
        String imageurl = product.getImages().get(0).getImage();
        String priceunitid = String.valueOf(pricesItem.getId());
        String price = String.valueOf(pricesItem.getSalePrice());
        String munit = String.valueOf(pricesItem.getUnit());
        String munitName = String.valueOf(pricesItem.getUnits().getTitle());

        Log.d("TAG", "setCartData: price u idqq " + priceunitid);
        Log.d("TAG", "setCartData: price munit " + munit);
        Log.d("TAG", "setCartData: price unitname " + munitName);

        long quantity = getCartdata(priceunitid);
        if (Const.getMaxQuantity() <= quantity) {
            Toast.makeText(context, "You reached Max Limit", Toast.LENGTH_SHORT).show();
            return;
        }

        if (quantity == 0) {
            quantity++;
            CartOffline cartOffline = new CartOffline(pid, quantity, price, name, imageurl, munit, munitName, priceunitid);
            db.cartDao().insertNew(cartOffline);
            Log.d("TAG", "setCartData: added new ");
        } else {
            quantity++;
            db.cartDao().updateObj(quantity, priceunitid);
            Log.d("TAG", "add: updated one");
        }

    }

    public long getCartdata(String id) {
        Log.d("qqqq1", "getCartdata: " + id);
        Log.d("TAG", "getCartdata: size  " + db.cartDao().getCartProduct(id).size());
        if (!db.cartDao().getCartProduct(id).isEmpty()) {
            return db.cartDao().getCartProduct(id).get(0).getQuantity();
        } else {
            return 0;
        }


    }

    public void less(long quantity, PricesItem pricesItem) {
        String priceunitid = String.valueOf(pricesItem.getId());
        db.cartDao().updateObj(quantity, priceunitid);
        long q = getCartdata(priceunitid);
        if (q < 1) {
            db.cartDao().deleteObjbyPid(priceunitid);
        }
    }

    public void less(long quantity, String priceunitid) {

        db.cartDao().updateObj(quantity, priceunitid);
        long q = getCartdata(priceunitid);
        if (q < 1) {
            db.cartDao().deleteObjbyPid(priceunitid);
        }
    }


    public void add(CartOffline product) {
        String pid = String.valueOf(product.getId());
        String name = product.getName();
        String imageurl = product.getImageUrl();
        String priceunitid = String.valueOf(product.getPriceUnitId());
        String price = String.valueOf(product.getPrice());
        String munit = String.valueOf(product.getPriceUnit());
        String munitName = String.valueOf(product.getPriceUnitName());

        Log.d("TAG", "setCartData: price u id " + priceunitid);
        Log.d("TAG", "setCartData: price munit " + munit);
        Log.d("TAG", "setCartData: price unitname " + munitName);

        long quantity = getCartdata(priceunitid);
        if (Const.MAX_QUANTITY <= quantity) {
            Toast.makeText(context, "You reached Max Limit", Toast.LENGTH_SHORT).show();
            return;
        }
        if (quantity == 0) {
            quantity++;
            CartOffline cartOffline = new CartOffline(pid, quantity, price, name, imageurl, munit, munitName, priceunitid);
            db.cartDao().insertNew(cartOffline);
            Log.d("TAG", "setCartData: added new ");
        } else {
            quantity++;
            db.cartDao().updateObj(quantity, priceunitid);
            Log.d("TAG", "add: updated one");
        }
    }
}
