package com.volkovmedia.perfo.firebaserealtimechat.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.volkovmedia.perfo.firebaserealtimechat.R;
import com.volkovmedia.perfo.firebaserealtimechat.objects.User;
import com.volkovmedia.perfo.firebaserealtimechat.utils.ImageValidator;
import com.volkovmedia.perfo.firebaserealtimechat.objects.Message;
import com.volkovmedia.perfo.firebaserealtimechat.utils.UniqueList;
import com.volkovmedia.perfo.firebaserealtimechat.view.adapters.viewholders.InterlocutorMessageViewHolder;
import com.volkovmedia.perfo.firebaserealtimechat.view.adapters.viewholders.UserMessageViewHolder;

import static com.volkovmedia.perfo.firebaserealtimechat.general.ChatApplication.getCurrentUserId;

public class MessageAdapter extends RecyclerView.Adapter<UserMessageViewHolder> {

    private final static int VIEWTYPE_USER = 101, VIEWTYPE_INTERLOCUTOR = 102;

    private UniqueList<Message> pMessages;
    private UniqueList<User> pUsers;

    private OnImageClickListener pListener;

    private ImageValidator pImageValidator;

    public MessageAdapter(UniqueList<Message> messages, UniqueList<User> users, OnImageClickListener listener) {
        this.pMessages = messages;
        this.pUsers = users;
        this.pListener = listener;
        this.pImageValidator = new ImageValidator();
    }

    @Override
    public int getItemViewType(int position) {
        return pMessages.get(position).getAuthorId().equals(getCurrentUserId()) ? VIEWTYPE_USER : VIEWTYPE_INTERLOCUTOR;
    }

    @Override
    public UserMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;

        switch (viewType) {
            case VIEWTYPE_USER:
                rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_user, parent, false);
                return new UserMessageViewHolder(rootView);

            case VIEWTYPE_INTERLOCUTOR:
            default:
                rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_interlocutor, parent, false);
                return new InterlocutorMessageViewHolder(rootView);
        }
    }

    @Override
    public void onBindViewHolder(UserMessageViewHolder holder, int position) {
        final Message message = pMessages.get(position);

        holder.setData(message, pUsers.get(message.getAuthorId()), pImageValidator);

        holder.getClickableView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pListener.onImageClick(view, message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pMessages.size();
    }

    public interface OnImageClickListener {

        void onImageClick(View view, Message data);

    }
}