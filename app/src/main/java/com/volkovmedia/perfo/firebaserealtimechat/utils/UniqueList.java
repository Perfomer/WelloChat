package com.volkovmedia.perfo.firebaserealtimechat.utils;

import com.volkovmedia.perfo.firebaserealtimechat.objects.DataContainer;

import java.util.ArrayList;
import java.util.HashSet;

public class UniqueList<T extends DataContainer> {

    private ArrayList<T> mList;
    private HashSet<String> mKeys;

    public UniqueList() {
        mList = new ArrayList<>();
        mKeys = new HashSet<>();
    }

    public boolean add(T item) {
        if (item != null) {
            String key = item.getId();

            if (!mKeys.contains(key)) {
                mKeys.add(key);
                mList.add(item);
                return true;
            }
        }

        return false;
    }

    public T get(int index) {
        return mList.get(index);
    }

    public T get(String key) {
        for (T item : mList) {
            if (item.getId().equals(key)) {
                return item;
            }
        }

        return null;
    }

    public int getPositionByKey(String key) {
        for (int i = 0; i < mKeys.size(); i++) {
            T item = mList.get(i);

            if (item.getId().equals(key)) {
                return i;
            }
        }

        return -1;
    }

    public boolean contains(String key) {
        return mKeys.contains(key);
    }

    public int size() {
        return mList.size();
    }

    public int indexOf(T item) {
        return mList.indexOf(item);
    }

    public ArrayList<T> toArrayList() {
        return mList;
    }

}
