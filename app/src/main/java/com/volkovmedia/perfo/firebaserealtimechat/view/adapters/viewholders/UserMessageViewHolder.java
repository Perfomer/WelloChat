package com.volkovmedia.perfo.firebaserealtimechat.view.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.volkovmedia.perfo.firebaserealtimechat.R;
import com.volkovmedia.perfo.firebaserealtimechat.general.GlideApp;
import com.volkovmedia.perfo.firebaserealtimechat.objects.User;
import com.volkovmedia.perfo.firebaserealtimechat.utils.TimeManager;
import com.volkovmedia.perfo.firebaserealtimechat.utils.ImageValidator;
import com.volkovmedia.perfo.firebaserealtimechat.objects.Message;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserMessageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.i_message_text)
    TextView pText;

    @BindView(R.id.i_message_time)
    TextView pTime;

    @BindView(R.id.i_message_nickname)
    TextView pNickname;

    @BindView(R.id.i_message_image)
    ImageView pImage;

    public UserMessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(Message message, User author, ImageValidator validator) {
        boolean isPicture = validator.validate(message.getText());

        int visibilityPlus = isPicture ? View.VISIBLE : View.GONE,
                visibilityMinus = isPicture ? View.GONE : View.VISIBLE;

        pText.setVisibility(visibilityMinus);
        pImage.setVisibility(visibilityPlus);

        if (isPicture) {
            GlideApp.with(pImage)
                    .load(message.getText())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .centerCrop()
                    .into(pImage);
        } else {
            pText.setText(message.getText());
        }

        pNickname.setText(author.getName());

        pTime.setText(TimeManager.convertTimestamp(message.getTimestamp()));
    }

    public View getClickableView() {
        return pImage;
    }
}
