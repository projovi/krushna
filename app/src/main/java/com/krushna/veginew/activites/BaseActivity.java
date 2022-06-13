package com.krushna.veginew.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.krushna.veginew.R;
import com.krushna.veginew.SessionManager;
import com.krushna.veginew.databinding.ItemLoginBinding;
import com.krushna.veginew.interfaces.LoginListnraer;
import com.krushna.veginew.models.UserRoot;
import com.krushna.veginew.popups.LoderPopup;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import java.text.DecimalFormat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1001;
    private static final String TAG = "baseactivity";
    SessionManager sessionManager;
    CompositeDisposable disposable = new CompositeDisposable();
    private BottomSheetDialog bottomSheetDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private UserRoot.User user;
    private LoginListnraer loginListnraer;
    public DecimalFormat df;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        df = new DecimalFormat("###.##");
        if (sessionManager.getUser() != null) {
            SessionManager.userToken = sessionManager.getUser().getToken();
            SessionManager.USER_ID = String.valueOf(sessionManager.getUser().getId());
        }
        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            user = sessionManager.getUser();
        }


    }

    public String getToken() {
        if (user != null) {
            return user.getToken();
        }
        return "";
    }

    public void openLoginSheet(LoginListnraer loginListnraer) {
        this.loginListnraer = loginListnraer;
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet)
                    .setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        ItemLoginBinding itemLoginBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.item_login, null, false);
        bottomSheetDialog.setContentView(itemLoginBinding.getRoot());
        bottomSheetDialog.show();

        itemLoginBinding.googlelogin.setOnClickListener(view -> {


            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            if (this != null) {
                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            }
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
        itemLoginBinding.btnclose.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            loginListnraer.onFailure();
        });
        itemLoginBinding.tvTerms.setOnClickListener(v -> {
            WebActivity.open(this, "Privacy Policy", sessionManager.getSetting().getPrivacyPolicy());

        });
        bottomSheetDialog.setOnDismissListener(dialog -> {
            loginListnraer.onDismiss();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if (account != null) {
                    registerUser(account);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getIdToken());


                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);

                // ...
            }
        }
    }

    private void registerUser(GoogleSignInAccount account) {
        LoderPopup loderPopup = new LoderPopup(this);
        disposable.add(RetrofitBuilder.create()
                .registerUser1(Const.DEVKEY, account.getEmail(), account.getEmail(), "xyz", account.getDisplayName()
                        , "gmail", "1", sessionManager.getStringValue(Const.NOTIFICATION_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {
                    loderPopup.show();
                })
                .doOnDispose(() -> {

                })
                .subscribe((userRoot, throwable) -> {
                    loderPopup.cencel();
                    if (userRoot != null && userRoot.isStatus()) {
                        sessionManager.saveStringValue(Const.USER_IMAGE, account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "");
                        sessionManager.saveUser(userRoot.getUser());
                        sessionManager.saveBooleanValue(Const.IS_LOGIN, true);
                        Toast.makeText(BaseActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        loginListnraer.onLoginSuccess(userRoot.getUser());

                    } else {
                        Toast.makeText(BaseActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    bottomSheetDialog.dismiss();
                })
        );


    }

}
