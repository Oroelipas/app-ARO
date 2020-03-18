package com.example.upnadeportes.registro;

import android.support.annotation.Nullable;

import com.example.upnadeportes.login.LoggedInUserView;

public class RegistroResult {

    @Nullable
    private LoggedInUserView success;

    @Nullable
    private Integer error;

    RegistroResult(@Nullable Integer error) {
        this.error = error;
    }

    RegistroResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
