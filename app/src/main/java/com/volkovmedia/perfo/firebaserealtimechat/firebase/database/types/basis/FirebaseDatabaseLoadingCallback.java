package com.volkovmedia.perfo.firebaserealtimechat.firebase.database.types.basis;

import com.volkovmedia.perfo.firebaserealtimechat.objects.DataContainer;

public interface FirebaseDatabaseLoadingCallback<T extends DataContainer> {

    void onLoadingSuccess(T result);

    void onLoadingError(String message);

}
