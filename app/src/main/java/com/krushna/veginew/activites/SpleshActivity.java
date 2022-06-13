package com.krushna.veginew.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.krushna.veginew.R;
import com.krushna.veginew.SessionManager;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SpleshActivity extends AppCompatActivity {
    SessionManager sessionManager;
    CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splesh);

        sessionManager = new SessionManager(this);

        if (sessionManager.getUser() != null) {
            SessionManager.userToken = sessionManager.getUser().getToken();
            SessionManager.USER_ID = String.valueOf(sessionManager.getUser().getId());
        }
        disposable.add(RetrofitBuilder.create().getSetting(Const.DEVKEY).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {

                })
                .doOnDispose(() -> {

                })
                .subscribe((settingRoot, throwable) -> {
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (settingRoot != null && settingRoot.isStatus() &&
                            settingRoot.getData() != null) {

                        sessionManager.saveSetting(settingRoot.getData());
                        Const.setCurrency(settingRoot.getData().getCurrencies());
                        Const.setMaxQuantity(settingRoot.getData().getMaxQuantity());
                        if (!sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                            initFirebaseMessage();
                        } else {

                            startActivity(new Intent(this, MainActivity.class));
                        }

                    } else {
                        Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }));


    }

    private void initFirebaseMessage() {
        FirebaseMessaging.getInstance().subscribeToTopic(Const.VEGI)
                .addOnCompleteListener(task -> {
                    Log.d("TAG", "onComplete: fcm");
                    if (!task.isSuccessful()) {
                        Log.d("TAG", "onComplete:failll " + task.toString());
                    } else {
                        Log.d("TAG", "success");
                    }
                });
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        sessionManager.saveStringValue(Const.NOTIFICATION_TOKEN, token);
                        // Log and toast
                        startActivity(new Intent(SpleshActivity.this, MainActivity.class));
                        Log.d("TAG    token===   ", token);

                    }
                });

    }
}