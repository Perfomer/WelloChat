package com.volkovmedia.perfo.firebaserealtimechat.firebase.database.types.basis;

import com.volkovmedia.perfo.firebaserealtimechat.objects.DataContainer;
import com.volkovmedia.perfo.firebaserealtimechat.objects.Message;

public interface FirebaseDatabaseObjectCallback<T extends DataContainer> {

    void onObjectAdded(T object);

    void onObjectLoadingFailed(String error);

}
