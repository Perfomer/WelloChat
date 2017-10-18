package com.volkovmedia.perfo.firebaserealtimechat.view.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;

import com.volkovmedia.perfo.firebaserealtimechat.R;
import com.volkovmedia.perfo.firebaserealtimechat.general.GlideApp;
import com.volkovmedia.perfo.firebaserealtimechat.objects.User;
import com.volkovmedia.perfo.firebaserealtimechat.utils.ImageValidator;
import com.volkovmedia.perfo.firebaserealtimechat.objects.Message;

import butterknife.BindView;

public class InterlocutorMessageViewHolder extends UserMessageViewHolder {

    @BindView(R.id.i_message_author_pic)
    ImageView iAuthorPicture;

    public InterlocutorMessageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Message message, User author, ImageValidator validator) {
        super.setData(message, author, validator);

        GlideApp.with(iAuthorPicture)
                .load(author.getPicture())
                .centerCrop()
                .into(iAuthorPicture);
    }

}