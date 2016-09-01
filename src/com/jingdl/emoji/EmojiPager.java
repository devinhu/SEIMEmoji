package com.jingdl.emoji;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * [一句话简单描述]
 *
 * @author huxinwu
 * @version 1.0
 * @date 2016/8/25
 */
public class EmojiPager {

    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout mIndicator;
    private int mPageCount;
    private int mSelectedPage;
    private EmojiPager.OnEmojiClickListener onEmojiClickListener;


    public EmojiPager(ViewGroup viewGroup) {
        this.mContext = viewGroup.getContext();
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.custom_viewpager, viewGroup);
        this.mViewPager = (ViewPager)view.findViewById(R.id.em_view_pager);
        this.mIndicator = (LinearLayout)view.findViewById(R.id.em_indicator);
        this.mPageCount = (int)Math.ceil((double)((float)FaceUtil.getInstace(mContext).emojis.size() / 20.0F));
        this.mViewPager.setAdapter(new EmojiViewPagerAdapter());
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageScrollStateChanged(int state) {}
            public void onPageSelected(int position) {
                EmojiPager.this.onIndicatorChanged(EmojiPager.this.mSelectedPage, position);
                EmojiPager.this.mSelectedPage = position;
            }
        });
        this.mViewPager.setCurrentItem(0, false);
        this.mViewPager.setOffscreenPageLimit(1);
        this.initIndicator(this.mPageCount, this.mIndicator);
        this.onIndicatorChanged(-1, 0);
    }

    private void initIndicator(int pages, LinearLayout indicator) {
        for(int i = 0; i < pages; ++i) {
            ImageView imageView = new ImageView(this.mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(0, 0, 10, 0);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.d1);
            indicator.addView(imageView);
        }
    }

    private void onIndicatorChanged(int pre, int cur) {
        int count = this.mIndicator.getChildCount();
        if(count > 0 && pre < count && cur < count) {
            ImageView curView;
            if(pre >= 0) {
                curView = (ImageView)this.mIndicator.getChildAt(pre);
                curView.setImageResource(R.drawable.d1);
            }
            if(cur >= 0) {
                curView = (ImageView)this.mIndicator.getChildAt(cur);
                curView.setImageResource(R.drawable.d2);
            }
        }
    }

    private EmojiAdapter emojiAdapter;

    private class EmojiViewPagerAdapter extends PagerAdapter {

        private EmojiViewPagerAdapter() {}

        public Object instantiateItem(ViewGroup container, int position) {
            EmojiPager.this.mIndicator.setVisibility(View.VISIBLE);
            emojiAdapter = new EmojiAdapter(EmojiPager.this.mContext, position * 20);
            emojiAdapter.setOnEmojiClickListener(onEmojiClickListener);
            GridView gridView = (GridView)LayoutInflater.from(container.getContext()).inflate(R.layout.emoji_gridview, (ViewGroup)null);
            gridView.setAdapter(emojiAdapter);
            container.addView(gridView);
            return gridView;
        }

        public int getItemPosition(Object object) {
            return -2;
        }

        public int getCount() {
            return EmojiPager.this.mPageCount == 0 ? 1 : EmojiPager.this.mPageCount;
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            View layout = (View)object;
            container.removeView(layout);
        }
    }

    public interface OnEmojiClickListener {
        void onEmojiClick(ChatEmoji emoji);
    }

    public OnEmojiClickListener getOnEmojiClickListener() {
        return onEmojiClickListener;
    }

    public void setOnEmojiClickListener(OnEmojiClickListener onEmojiClickListener) {
        this.onEmojiClickListener = onEmojiClickListener;
    }
}
