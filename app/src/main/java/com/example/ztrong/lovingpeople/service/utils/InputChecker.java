package com.example.ztrong.lovingpeople.service.utils;

import android.view.View;

public class InputChecker {
    private boolean isCancel;
    private View focusView;

    public InputChecker() {
        isCancel = false;
        focusView = null;
    }

    public InputChecker(boolean isCancel, View focusView) {
        this.isCancel = isCancel;
        this.focusView = focusView;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public View getFocusView() {
        return focusView;
    }

    public void setFocusView(View focusView) {
        this.focusView = focusView;
    }
}

