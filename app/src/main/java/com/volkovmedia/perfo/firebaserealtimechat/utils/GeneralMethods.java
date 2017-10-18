package com.volkovmedia.perfo.firebaserealtimechat.utils;

import com.volkovmedia.perfo.firebaserealtimechat.objects.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GeneralMethods {

    public static void sortMessages(ArrayList<Message> list) {
        Collections.sort(list, new Comparator<Message>() {
            @Override
            public int compare(Message lhs, Message rhs) {
                return lhs.getTimestamp() < rhs.getTimestamp() ? -1 : (lhs.getTimestamp() > rhs.getTimestamp()) ? 1 : 0;
            }
        });
    }

}
