package com.jingdl.emoji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

import com.jingdl.emoji.R;

/**
 * *****************************************
 *
 * @author 廖乃波
 * @文件名称 : FaceConversionUtil.java
 * @创建时间 : 2013-1-27 下午02:34:09
 * @文件描述 : 表情轉換工具
 * *****************************************
 */
public class FaceUtil {

    /**
     * 每一页表情的个数
     */
    private int pageSize = 20;

    private static FaceUtil mFaceConversionUtil;

    /**
     * 保存于内存中的表情HashMap
     */
    private HashMap<String, String> emojiMap = new HashMap<String, String>();

    /**
     * 保存于内存中的表情集合
     */
    public List<ChatEmoji> emojis = new ArrayList<ChatEmoji>();


    private FaceUtil(Context context) {
        ParseData(getEmojiFile(), context);
    }

    public static FaceUtil getInstace(Context context) {
        if (mFaceConversionUtil == null) {
            mFaceConversionUtil = new FaceUtil(context);
        }
        return mFaceConversionUtil;
    }

    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     *
     * @param context
     * @param str
     * @return
     */
    public SpannableString getExpressionString(Context context, String str) {
        SpannableString spannableString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = "\\[[^\\]]+\\]";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(context, spannableString, sinaPatten, 0);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return spannableString;
    }

    /**
     * 添加表情
     *
     * @param context
     * @param imgId
     * @param spannableString
     * @return
     */
    public SpannableString addFace(Context context, int imgId, String spannableString) {
        if (TextUtils.isEmpty(spannableString)) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgId);
        bitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, true);
        ImageSpan imageSpan = new ImageSpan(context, bitmap);
        SpannableString spannable = new SpannableString(spannableString);
        spannable.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context
     * @param spannableString
     * @param patten
     * @param start
     * @throws Exception
     */
    private void dealExpression(Context context, SpannableString spannableString, Pattern patten, int start) throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
            if (matcher.start() < start) {
                continue;
            }
            String value = emojiMap.get(key);
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            int resId = context.getResources().getIdentifier(value, "drawable", context.getPackageName());
            // 通过上面匹配得到的字符串来生成图片资源id
            // Field field=R.drawable.class.getDeclaredField(value);
            // int resId=Integer.parseInt(field.get(null).toString());
            if (resId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                bitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
                // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                ImageSpan imageSpan = new ImageSpan(bitmap);
                // 计算该图片名字的长度，也就是要替换的字符串的长度
                int end = matcher.start() + key.length();
                // 将该图片替换字符串中规定的位置中
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                if (end < spannableString.length()) {
                    // 如果整个字符串还未验证完，则继续。。
                    dealExpression(context, spannableString, patten, end);
                }
                break;
            }
        }
    }

    /**
     * 解析字符
     *
     * @param data
     */
    private void ParseData(List<String> data, Context context) {
        if (data == null) {
            return;
        }
        ChatEmoji emojEentry;
        try {
            for (String str : data) {
                String[] text = str.split(",");
                String fileName = text[0].substring(0, text[0].lastIndexOf("."));
                emojiMap.put(text[1], fileName);
                int resID = context.getResources().getIdentifier(fileName, "drawable", context.getPackageName());

                if (resID != 0) {
                    emojEentry = new ChatEmoji();
                    emojEentry.setId(resID);
                    emojEentry.setCharacter(text[1]);
                    emojEentry.setFaceName(fileName);
                    emojis.add(emojEentry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取表情配置文件
     * @return
     */
    private List<String> getEmojiFile() {
        List<String> list = new ArrayList<String>();
        list.add("emoji_1.png,[微笑]");
        list.add("emoji_2.png,[惊吓]");
        list.add("emoji_3.png,[色]");
        list.add("emoji_4.png,[呆萌]");
        list.add("emoji_5.png,[酷]");
        list.add("emoji_6.png,[流泪]");
        list.add("emoji_7.png,[满足]");
        list.add("emoji_8.png,[刺瞎]");
        list.add("emoji_9.png,[睡觉]");
        list.add("emoji_10.png,[委屈]");
        list.add("emoji_11.png,[流汗]");
        list.add("emoji_12.png,[怒]");
        list.add("emoji_13.png,[调皮]");
        list.add("emoji_14.png,[孔孔]");
        list.add("emoji_15.png,[哈欠]");
        list.add("emoji_16.png,[啊]");
        list.add("emoji_17.png,[气死]");
        list.add("emoji_18.png,[无力]");
        list.add("emoji_19.png,[不嘛]");
        list.add("emoji_20.png,[难受]");
        list.add("emoji_21.png,[恶魔]");
        list.add("emoji_22.png,[眨眼]");
        list.add("emoji_23.png,[郁闷]");
        list.add("emoji_24.png,[傲慢]");
        list.add("emoji_25.png,[害怕]");
        list.add("emoji_26.png,[饿了]");
        list.add("emoji_27.png,[气死你]");
        list.add("emoji_28.png,[汗]");
        list.add("emoji_29.png,[大笑]");
        list.add("emoji_30.png,[大哭]");
        list.add("emoji_31.png,[飞吻]");
        list.add("emoji_32.png,[无语]");
        list.add("emoji_33.png,[坏笑]");
        list.add("emoji_34.png,[囧]");
        list.add("emoji_35.png,[酱紫]");
        list.add("emoji_36.png,[恐怖]");
        list.add("emoji_37.png,[疯了]");
        list.add("emoji_38.png,[我不要]");
        list.add("emoji_39.png,[失望]");
        list.add("emoji_40.png,[好舒服]");
        list.add("emoji_41.png,[萌萌哒]");
        list.add("emoji_42.png,[困]");
        list.add("emoji_43.png,[天使]");
        list.add("emoji_44.png,[不说]");
        list.add("emoji_45.png,[拳头]");
        list.add("emoji_46.png,[鄙视]");
        list.add("emoji_47.png,[上面]");
        list.add("emoji_48.png,[胜利]");
        list.add("emoji_49.png,[纳尼]");
        list.add("emoji_50.png,[生病]");
        list.add("emoji_51.png,[不看]");
        list.add("emoji_52.png,[OK]");
        list.add("emoji_53.png,[鼓掌]");
        list.add("emoji_54.png,[强壮]");
        list.add("emoji_55.png,[加油]");
        list.add("emoji_56.png,[矮油]");
        list.add("emoji_57.png,[笑脸]");
        list.add("emoji_58.png,[不听]");
        list.add("emoji_59.png,[赞]");
        list.add("emoji_60.png,[祈祷]");
        list.add("emoji_61.png,[手掌]");
        list.add("emoji_62.png,[太阳]");
        list.add("emoji_63.png,[咖啡]");
        list.add("emoji_64.png,[雪人]");
        list.add("emoji_65.png,[书]");
        list.add("emoji_66.png,[惊喜礼物]");
        list.add("emoji_67.png,[礼花]");
        list.add("emoji_68.png,[冰激凌]");
        list.add("emoji_69.png,[云朵]");
        list.add("emoji_70.png,[雪花]");
        list.add("emoji_71.png,[闪电]");
        list.add("emoji_72.png,[金钱]");
        list.add("emoji_73.png,[蛋糕]");
        list.add("emoji_74.png,[毕业]");
        list.add("emoji_75.png,[肘子]");
        list.add("emoji_76.png,[打伞]");
        list.add("emoji_77.png,[多云]");
        list.add("emoji_78.png,[笔笔]");
        list.add("emoji_79.png,[便便]");
        list.add("emoji_80.png,[圣诞树]");
        list.add("emoji_81.png,[红酒]");
        list.add("emoji_82.png,[麦克风]");
        list.add("emoji_83.png,[篮球]");
        list.add("emoji_84.png,[中]");
        list.add("emoji_85.png,[炸弹]");
        list.add("emoji_86.png,[喇叭]");
        list.add("emoji_87.png,[地球]");
        list.add("emoji_88.png,[巧克力]");
        list.add("emoji_89.png,[骰子]");
        list.add("emoji_90.png,[滑雪]");
        list.add("emoji_91.png,[灯泡]");
        list.add("emoji_92.png,[ZZZ]");
        list.add("emoji_93.png,[禁止]");
        list.add("emoji_94.png,[向日葵]");
        list.add("emoji_95.png,[啤酒]");
        list.add("emoji_96.png,[音乐]");
        list.add("emoji_97.png,[房子]");
        list.add("emoji_98.png,[杯贴]");
        list.add("emoji_99.png,[电话]");
        list.add("emoji_100.png,[淋浴]");
        list.add("emoji_101.png,[米饭]");
        list.add("emoji_102.png,[全家福]");
        list.add("emoji_103.png,[美丽天使]");
        list.add("emoji_104.png,[感冒]");
        list.add("emoji_105.png,[手枪]");
        list.add("emoji_106.png,[玫瑰]");
        list.add("emoji_107.png,[小狗]");
        list.add("emoji_108.png,[口红]");
        list.add("emoji_109.png,[结婚]");
        list.add("emoji_110.png,[外星人]");
        list.add("emoji_111.png,[吻]");
        list.add("emoji_112.png,[月牙]");
        list.add("emoji_113.png,[习惯]");
        list.add("emoji_114.png,[猪头]");
        list.add("emoji_115.png,[心碎]");
        list.add("emoji_116.png,[幽灵]");
        list.add("emoji_118.png,[钻戒]");
        list.add("emoji_119.png,[松树]");
        list.add("emoji_120.png,[小马]");
        list.add("emoji_121.png,[王冠]");
        list.add("emoji_122.png,[火焰]");
        list.add("emoji_123.png,[五角星]");
        list.add("emoji_124.png,[足球]");
        list.add("emoji_125.png,[时间]");
        list.add("emoji_126.png,[闹钟]");
        list.add("emoji_128.png,[火箭]");
        list.add("emoji_129.png,[漏斗]");
        list.add("emoji_130.png,[自行车]");
        return list;
    }
}