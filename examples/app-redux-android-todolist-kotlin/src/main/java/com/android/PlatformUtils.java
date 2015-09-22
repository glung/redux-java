package com.android;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import javax.inject.Inject;

class PlatformUtils {

    private final InputMethodManager imm;

    @Inject
    PlatformUtils(InputMethodManager imm) {
        this.imm = imm;
    }

    void hideKeyboard(View view) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    void showKeyboard(View view) {
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }
}
