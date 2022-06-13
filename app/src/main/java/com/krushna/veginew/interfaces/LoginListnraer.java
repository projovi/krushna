package com.krushna.veginew.interfaces;

import com.krushna.veginew.models.UserRoot;

public interface LoginListnraer {
    void onLoginSuccess(UserRoot.User user);

    void onDismiss();

    void onFailure();
}
