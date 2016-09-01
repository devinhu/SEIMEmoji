  
 Emoji使用指南
  
 ##页面布局布局：
 ```javascript
 <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context="com.example.emojidemo.MainActivity" >

    <com.jingdl.emoji.EmojiLayout
        android:id="@+id/emojiLayout"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"/>

</RelativeLayout>
 ```
 
##Activity中调用：
```javascript
EmojiLayout emojiLayout = getViewById(R.id.emojiLayout);
emojiLayout.setOnResultListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
		emojiLayout.dimiss();
		comment = v.getTag().toString();
	}
});

@Override
public boolean dispatchTouchEvent(MotionEvent event) {
	if(event.getAction() == MotionEvent.ACTION_DOWN){
		//点击非emojiLayout区域隐藏emojiLayout
		if(!emojiLayout.inRangeOfView(event)){
			emojiLayout.dimiss();
			//隐藏键盘，自己写一个吧
			CommonUtils.hideKeyboard(ArticleDetailActivity.this);
		}
	}
	return super.dispatchTouchEvent(event);
}
```

##TextView显示emoji表情
```javascript
tv_item_content.setText(FaceUtil.getInstace(mContext).getExpressionString(mContext, Data.getContent()));
```
