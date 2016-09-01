package com.jingdl.emoji;


import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * [一句话简单描述]
 *
 * @author huxinwu
 * @version 1.0
 * @date 2016/8/17
 */
public class EmojiLayout extends LinearLayout {

    private Button btn_send;
    private ImageView img_emoji;
    private EditText edit_emoji_text;
    private FrameLayout layout_emojikeyboard;
    private View.OnClickListener onResultListener;
    private EmojiPager emojiPager;

    public EmojiLayout(Context context) {
        super(context);
        init(context);
    }

    public EmojiLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(final Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.custom_facelayout1, null);
        addView(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        layout_emojikeyboard = (FrameLayout) view.findViewById(R.id.layout_emojikeyboard);
        layout_emojikeyboard.setVisibility(GONE);
        emojiPager = new EmojiPager(layout_emojikeyboard);
        emojiPager.setOnEmojiClickListener(new EmojiPager.OnEmojiClickListener() {
            @Override
            public void onEmojiClick(ChatEmoji emoji) {
                if (emoji.getId() == R.drawable.em_ic_delete) {
                    int selection = edit_emoji_text.getSelectionStart();
                    String text = edit_emoji_text.getText().toString();
                    if (selection > 0) {
                        String text2 = text.substring(selection - 1);
                        if ("]".equals(text2)) {
                            int start = text.lastIndexOf("[");
                            int end = selection;
                            edit_emoji_text.getText().delete(start, end);
                            return;
                        }
                        edit_emoji_text.getText().delete(selection - 1, selection);
                    }
                }
                if (!TextUtils.isEmpty(emoji.getCharacter())) {
                    SpannableString spannableString = FaceUtil.getInstace(context).addFace(getContext(), emoji.getId(), emoji.getCharacter());
                    edit_emoji_text.append(spannableString);
                }
            }
        });

        img_emoji = (ImageView) view.findViewById(R.id.img_emoji);
        img_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                img_emoji.setSelected(true);
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_emoji_text.getWindowToken(), 0);
                layout_emojikeyboard.setVisibility(View.VISIBLE);
            }
        });

        edit_emoji_text = (EditText) view.findViewById(R.id.edit_emoji_text);
        edit_emoji_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case KeyEvent.ACTION_DOWN:
                        edit_emoji_text.setSelected(true);
                        layout_emojikeyboard.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInputFromInputMethod(edit_emoji_text.getWindowToken(), 0);
                        return false;
                }
                return false;
            }
        });

        btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = edit_emoji_text.getText().toString();
                if(TextUtils.isEmpty(result)){
                    return;
                }
                if(onResultListener != null){
                    v.setTag(result);
                    onResultListener.onClick(v);
                    edit_emoji_text.setText("");
                    img_emoji.setSelected(false);
                    edit_emoji_text.setSelected(false);
                }
            }
        });
    }


    public void dimiss(){
        layout_emojikeyboard.setVisibility(View.GONE);
        img_emoji.setSelected(false);
        edit_emoji_text.setSelected(false);
    }

    public OnClickListener getOnResultListener() {
        return onResultListener;
    }

    public void setOnResultListener(OnClickListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    public boolean inRangeOfView(MotionEvent ev){
        int[] location = new int[2];
        EmojiLayout.this.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if(ev.getX() < x || ev.getX() > (x + EmojiLayout.this.getWidth()) || ev.getY() < y || ev.getY() > (y + EmojiLayout.this.getHeight())){
            return false;
        }
        return true;
    }
}
