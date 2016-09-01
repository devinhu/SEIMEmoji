  布局：
 <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context="com.example.emojidemo.MainActivity" >

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical" >

         <com.suneee.emoji.EmojiEditText
            android:id="@+id/edit_emoji"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/hello_world" />
         
        <com.suneee.emoji.EmojiKeyboard
            android:id="@+id/layout_emoji"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/hello_world" />
    </LinearLayout>

</RelativeLayout>
 
 
 Activity中调用：
 edit_emoji = (EmojiEditText)findViewById(R.id.edit_emoji);
        EmojiKeyboard layout_emoji = (EmojiKeyboard)findViewById(R.id.layout_emoji);
        layout_emoji.setEventListener(new EventListener() {
			
			@Override
			public void onEmojiSelected(String res) {
				EmojiKeyboard.input(edit_emoji, res);
			}
			
			@Override
			public void onBackspace() {
				EmojiKeyboard.backspace(edit_emoji);
			}
		});