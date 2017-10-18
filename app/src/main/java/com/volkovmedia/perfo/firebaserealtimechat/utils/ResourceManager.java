package com.volkovmedia.perfo.firebaserealtimechat.utils;

import android.content.Context;
import android.content.res.Resources;

public class ResourceManager {

    public static String getString(Context context, int resId) {
        Resources resources = context.getResources();
        return resources.getString(resId);
    }

}
