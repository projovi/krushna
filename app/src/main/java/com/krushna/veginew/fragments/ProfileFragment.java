package com.krushna.veginew.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.krushna.veginew.BuildConfig;
import com.krushna.veginew.R;
import com.krushna.veginew.activites.BaseActivity;
import com.krushna.veginew.activites.ComplainActivity;
import com.krushna.veginew.activites.DeliveryAddressActivity;
import com.krushna.veginew.activites.MainActivity;
import com.krushna.veginew.activites.MyOrdersActivity;
import com.krushna.veginew.activites.SpleshActivity;
import com.krushna.veginew.activites.WishlistActivity;
import com.krushna.veginew.databinding.BottomSheetEditprofileBinding;
import com.krushna.veginew.databinding.FragmentProfileBinding;
import com.krushna.veginew.interfaces.LoginListnraer;
import com.krushna.veginew.models.UserRoot;
import com.krushna.veginew.popups.ConfirmationPopup;
import com.krushna.veginew.popups.LoderPopup;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;
import com.krushna.veginew.utils.Compressor;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.MediaColumns.DATA;


public class ProfileFragment extends BaseFragment {


    private static final int GALLERY_CODE = 1001;
    private static final int PERMISSION_REQUEST_CODE = 2002;
    FragmentProfileBinding binding;
    private UserRoot.User user;
    CompositeDisposable disposable = new CompositeDisposable();
    private BottomSheetDialog bottomSheetDialog;
    BottomSheetEditprofileBinding editprofileBinding;
    private Uri selectedImage;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {

            setData(sessionManager.getUser());
            initView();
        } else {
            if (getActivity() != null) {

                ((BaseActivity) getActivity()).openLoginSheet(new LoginListnraer() {
                    @Override
                    public void onLoginSuccess(UserRoot.User u) {

                        setData(u);
                    }

                    @Override
                    public void onFailure() {

                    }

                    @Override
                    public void onDismiss() {
                        if (getActivity() == null) return;
                        ((MainActivity) getActivity()).binding.menuhome.performClick();
                    }
                });
            }
        }

        return binding.getRoot();
    }

    public void setData(UserRoot.User user) {
        if (getActivity() == null) return;
        this.user = user;
        Glide.with(getActivity()).load(sessionManager.getStringValue(Const.USER_IMAGE)).circleCrop()
                .placeholder(R.drawable.user_place_holder)
                .error(R.drawable.user_place_holder).into(binding.lytimage);
        binding.tvName.setText(user.getFirstname());
        binding.tvEmail.setText(user.getEmail());
    }

    private String picturePath = "";

    private void initView() {


        binding.lytmyorders.setOnClickListener(v -> startActivity(new Intent(getActivity(), MyOrdersActivity.class)));
        binding.lytmyaddress.setOnClickListener(v -> startActivity(new Intent(getActivity(), DeliveryAddressActivity.class)));
        binding.lytWishlist.setOnClickListener(v -> startActivity(new Intent(getActivity(), WishlistActivity.class)));
        binding.lytComplain.setOnClickListener(v -> startActivity(new Intent(getActivity(), ComplainActivity.class)));
        binding.lytLogout.setOnClickListener(v -> openConfirmDialog());
        binding.imgEdit.setOnClickListener(v -> openUpdateSheet());
        binding.lytRefer.setOnClickListener(v -> {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "Share app");
            share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);

            startActivity(Intent.createChooser(share, "Share link!"));
        });

    }

    private void openUpdateSheet() {
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        editprofileBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.bottom_sheet_editprofile, null, false);
        bottomSheetDialog.setContentView(editprofileBinding.getRoot());
        bottomSheetDialog.show();

        editprofileBinding.etName.setText(user.getFirstname());
        editprofileBinding.etEmail.setText(user.getEmail());
        Glide.with(binding.getRoot().getContext())
                .load(sessionManager.getStringValue(Const.USER_IMAGE))

                .placeholder(R.drawable.user_place_holder)
                .error(R.drawable.user_place_holder).circleCrop()
                //  .placeholder(R.drawable.delivery_placeholder)
                //  .error(R.drawable.delivery_placeholder)


                .into(editprofileBinding.imgUser);
        editprofileBinding.btnPencil.setOnClickListener(v -> choosePhoto());
        editprofileBinding.imgUser.setOnClickListener(v -> choosePhoto());
        editprofileBinding.btnclose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        editprofileBinding.tvSubmit.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {
        if (editprofileBinding.etName.getText().toString().isEmpty()) {

            Toast.makeText(context, "Enter Name First", Toast.LENGTH_SHORT).show();
            return;
        }
        if (editprofileBinding.etEmail.getText().toString().isEmpty()) {

            Toast.makeText(context, "Enter Email First", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody firstname = RequestBody.create(MediaType.parse("text/plain"), editprofileBinding.etName.getText().toString());
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sessionManager.getUser().getId()));
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), editprofileBinding.etEmail.getText().toString());

        HashMap<String, RequestBody> map = new HashMap<>();

        MultipartBody.Part body = null;
        if (picturePath != null && !picturePath.isEmpty()) {
            File file = new File(picturePath);
            Compressor compressor = Compressor.getDefault(getActivity());
            File compressedFile = compressor.compressToFile(file);

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), compressedFile);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        }
        map.put("firstname", firstname);
        map.put("email", email);
        map.put("id", id);
        LoderPopup loderPopup = new LoderPopup(getActivity());
        disposable.add(RetrofitBuilder.create()
                .updateUser(Const.DEVKEY, getToken(), map, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {
                    loderPopup.show();
                })
                .doOnDispose(() -> {

                })
                .subscribe((userRoot, throwable) -> {

                    if (userRoot != null && userRoot.isStatus()) {
                        sessionManager.saveStringValue(Const.USER_IMAGE, Const.IMAGE_URL + userRoot.getUser().getImage());
                        sessionManager.saveUser(userRoot.getUser());
                        sessionManager.saveBooleanValue(Const.IS_LOGIN, true);
                        setData(userRoot.getUser());
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    loderPopup.cencel();
                    bottomSheetDialog.dismiss();
                })
        );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {

            selectedImage = data.getData();

            Glide.with(getActivity())
                    .load(selectedImage)
                    .circleCrop()
                    .placeholder(R.drawable.user_place_holder)
                    .error(R.drawable.user_place_holder)
                    .into(editprofileBinding.imgUser);
            String[] filePathColumn = {DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();


        }
    }

    private void choosePhoto() {
        if (checkPermission()) {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, GALLERY_CODE);
        } else {
            requestPermission();
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), getString(R.string.permissonmsg), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
                choosePhoto();
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
        }
    }

    private void openConfirmDialog() {
        if (getActivity() == null) return;
        new ConfirmationPopup(getActivity(), "Log Out", "Are you really want to logout?", new ConfirmationPopup.OnPopupClickListner() {
            @Override
            public void onPositive() {
                logoutUser();
            }

            @Override
            public void onNegative() {

            }
        }, "Yes", "No");


    }

    private void logoutUser() {
        disposable.add(RetrofitBuilder.create()
                        .logout(Const.DEVKEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {
                    binding.pd.setVisibility(View.VISIBLE);
                })
                .doOnDispose(() -> {

                })
                .subscribe((restResponse, throwable) -> {
                    binding.pd.setVisibility(View.GONE);
                    if (restResponse != null && restResponse.isStatus()) {
                        sessionManager.saveStringValue(Const.USER_IMAGE, "");
                        sessionManager.saveUser(null);
                        sessionManager.saveBooleanValue(Const.IS_LOGIN, false);
                        Toast.makeText(getActivity(), "Logout Success", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .build();
                        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                        mGoogleSignInClient.signOut();

                        startActivity(new Intent(getActivity(), SpleshActivity.class));

                        if (getActivity() != null)
                            getActivity().finishAffinity();
                    } else {
                        if (restResponse != null) {
//                            Toast.makeText(getActivity(), restResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );

    }
}