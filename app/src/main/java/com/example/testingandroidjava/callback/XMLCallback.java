package com.example.testingandroidjava.callback;

import com.example.testingandroidjava.data.Message;
import com.example.testingandroidjava.data.WaktuSolatList;

public interface XMLCallback {
    void onSuccess (WaktuSolatList result);
    void onError (Message result);
}
