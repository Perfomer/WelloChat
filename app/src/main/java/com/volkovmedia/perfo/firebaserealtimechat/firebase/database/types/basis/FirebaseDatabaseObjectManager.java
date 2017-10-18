package com.volkovmedia.perfo.firebaserealtimechat.firebase.database.types.basis;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.volkovmedia.perfo.firebaserealtimechat.firebase.database.FirebaseDatabaseManager;
import com.volkovmedia.perfo.firebaserealtimechat.objects.DataContainer;

import java.util.HashSet;

public class FirebaseDatabaseObjectManager<T extends DataContainer> extends FirebaseDatabaseManager {

    private DatabaseReference mReference;
    private ChildEventListener mChildEventListener;
    private FirebaseDatabaseObjectCallback<T> mCallback;

    private Class<T> mObjectType;

    private HashSet<String> mLoadingHeap;

    public FirebaseDatabaseObjectManager(FirebaseDatabaseObjectCallback<T> callback, String reference, Class<T> objectType) {
        super();
        init(reference, objectType);
        this.mCallback = callback;

        if (mCallback != null) {
            this.mChildEventListener = new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    T object = castDataSnapshotToObject(dataSnapshot);

                    if (object != null)
                        mCallback.onObjectAdded(object);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mCallback.onObjectLoadingFailed(databaseError.getMessage());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) { }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            };
        }
    }

    public FirebaseDatabaseObjectManager(String reference, Class<T> objectType) {
        init(reference, objectType);
    }

    private void init(String reference, Class<T> objectType) {
        this.mReference = mDatabase.getReference(reference);
        this.mLoadingHeap = new HashSet<>();
        this.mObjectType = objectType;
    }

    public void switchListener(boolean makeActive) {
        if (mChildEventListener != null) {
            if (makeActive) {
                mReference.addChildEventListener(mChildEventListener);
            } else {
                mReference.removeEventListener(mChildEventListener);
            }
        }
    }

    public void send(T object) {
        mReference.push().setValue(object);
    }

    public void send(T object, String key) {
        mReference.child(key).setValue(object);
    }

    public void query(String key, final FirebaseDatabaseLoadingCallback<T> callback) {
        if (mLoadingHeap.contains(key)) return;
        else mLoadingHeap.add(key);

        DatabaseReference reference = mReference.child(key);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                T object = castDataSnapshotToObject(dataSnapshot);

                if (object != null)
                    callback.onLoadingSuccess(object);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onLoadingError(databaseError.getMessage());
            }
        });
    }

    private T castDataSnapshotToObject(DataSnapshot dataSnapshot) {
        T object = dataSnapshot.getValue(mObjectType);

        if (object != null) {
            object.setId(dataSnapshot.getKey());
        }

        return object;
    }

}