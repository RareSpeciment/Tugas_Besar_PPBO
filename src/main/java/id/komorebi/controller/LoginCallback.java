package id.komorebi.controller;

import id.komorebi.model.User;

public interface LoginCallback {
    void onLoginSuccess(User user);
}
