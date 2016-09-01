package com.jingdl.emoji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * [一句话简单描述]
 *
 * @author huxinwu
 * @version 1.0
 * @date 2016/8/25
 */
public class EmojiAdapter extends BaseAdapter {

    private static final int EMOJI_PER_PAGE = 20;
    private Context mContext;
    private int mStartIndex;
    private EmojiPager.OnEmojiClickListener onEmojiClickListener;

    public EmojiAdapter(Context context, int startIndex) {
        this.mContext = context;
        this.mStartIndex = startIndex;
    }

    public int getCount() {
        int count = FaceUtil.getInstace(mContext).emojis.size() - this.mStartIndex + 1;
        count = Math.min(count, 21);
        return count;
    }

    public Object getItem(int position) {
        ChatEmoji e = (ChatEmoji) FaceUtil.getInstace(mContext).emojis.get(position + this.mStartIndex);
        return e;
    }

    public long getItemId(int position) {
        return (long) (this.mStartIndex + position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.emoji_item, (ViewGroup) null);
        }

        ImageView emoji = (ImageView) convertView.findViewById(R.id.img_emoji_item);
        int count = FaceUtil.getInstace(mContext).emojis.size();
        int index = position + this.mStartIndex;
        if (position != 20 && index != count) {
            if (index < count) {
                final ChatEmoji e = (ChatEmoji) FaceUtil.getInstace(mContext).emojis.get(index);
                emoji.setImageResource(e.getId());
                emoji.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onEmojiClickListener != null){
                            onEmojiClickListener.onEmojiClick(e);
                        }
                    }
                });
            }
        } else {
            emoji.setImageResource(R.drawable.em_ic_delete);
            emoji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onEmojiClickListener != null){
                        ChatEmoji e = new ChatEmoji();
                        e.setId(R.drawable.em_ic_delete);
                        onEmojiClickListener.onEmojiClick(e);
                    }
                }
            });
        }

        return convertView;
    }

    public EmojiPager.OnEmojiClickListener getOnEmojiClickListener() {
        return onEmojiClickListener;
    }

    public void setOnEmojiClickListener(EmojiPager.OnEmojiClickListener onEmojiClickListener) {
        this.onEmojiClickListener = onEmojiClickListener;
    }
}