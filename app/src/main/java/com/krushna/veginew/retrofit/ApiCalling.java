package com.krushna.veginew.retrofit;

import android.content.Context;

import com.krushna.veginew.SessionManager;
import com.krushna.veginew.models.AeraRoot;
import com.krushna.veginew.models.CityRoot;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ApiCalling {
    String userId;
    private Context context;
    CompositeDisposable disposable = new CompositeDisposable();

    public ApiCalling(Context context) {

        this.context = context;
        userId = String.valueOf(new SessionManager(context).getUser().getId());
    }

    public void getCities(OnCitiyGetListner onCitiyGetListner) {
        disposable.add(RetrofitBuilder.create().getCities(Const.DEVKEY).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {

                })
                .doOnDispose(() -> {

                })
                .subscribe((cityRoot, throwable) -> {
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (cityRoot != null && cityRoot.isStatus() && !cityRoot.getData().isEmpty()) {
                        onCitiyGetListner.onFetched(cityRoot.getData());
                    } else {
                        onCitiyGetListner.onFail();
                    }
                }));

    }

    public void getAeraByCity(int id, OnAreaGetListner onAreaGetListner) {
        disposable.add(RetrofitBuilder.create().getAreaByCity(Const.DEVKEY, id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {

                })
                .doOnDispose(() -> {

                })
                .subscribe((areaRoot, throwable) -> {
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (areaRoot != null && areaRoot.isStatus() && !areaRoot.getData().isEmpty()) {
                        onAreaGetListner.onFetched(areaRoot.getData());
                    } else {
                        onAreaGetListner.onFail();
                    }
                }));
    }

    public interface OnCitiyGetListner {
        void onFetched(List<CityRoot.DataItem> data);

        void onFail();
    }

    public interface OnAreaGetListner {
        void onFetched(List<AeraRoot.DataItem> data);

        void onFail();
    }

}
