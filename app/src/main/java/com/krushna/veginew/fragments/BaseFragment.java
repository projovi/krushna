package com.krushna.veginew.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.krushna.veginew.SessionManager;
import com.krushna.veginew.models.UserRoot;
import com.krushna.veginew.retrofit.Const;

public abstract class BaseFragment extends Fragment {
    SessionManager sessionManager;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            sessionManager = new SessionManager(getActivity());
            context = getActivity();
        }
    }

    public String getToken() {
        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            UserRoot.User user = sessionManager.getUser();
            if (user != null) {
                return user.getToken();
            }

        }
        return "";
    }

}
