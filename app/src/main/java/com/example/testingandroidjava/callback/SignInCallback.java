package com.example.testingandroidjava.callback;

import com.example.testingandroidjava.data.Message;
import com.example.testingandroidjava.data.SignIn;
import com.example.testingandroidjava.data.SignInTFA;

public interface SignInCallback {
    void onSuccessTFA(SignInTFA result);
    void onSuccess(SignIn result);
    void onError(Message result);
}
