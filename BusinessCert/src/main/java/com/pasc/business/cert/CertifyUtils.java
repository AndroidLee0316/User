package com.pasc.business.cert;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.pasc.lib.widget.ClearEditText;

import java.util.List;

public class CertifyUtils {
    public static String getTvContent(TextView textView) {
        if (textView == null) {
            return "";
        }
        return textView.getText().toString().trim();
    }

    public static String replaceSpace(ClearEditText view) {
        if (view == null) {
            return "";
        }
        return view.getText().toString().replace(" ", "");
    }

    public static int getTvLength(TextView textView) {
        return getTvContent(textView).length();
    }

    /**
     * @return true: 内容为空, false: 内容不为空
     */
    public static boolean isEmpty(String msg) {
        return TextUtils.isEmpty(msg);
    }

    /**
     * @return true: 内容为空, false: 内容不为空
     */
    public static boolean isEmpty(TextView textView) {
        return textView == null || isEmpty(getTvContent(textView));
    }

    /**
     * @param idCard 身份证
     * @return 对身份证显示进行加密
     */
    public static String getEncryptedIdCard(String idCard) {
        if (isEmpty(idCard) || idCard.length() <= 2) {
            return "";
        }
        int length = idCard.length();
        String tidCard = idCard.substring(0, 1) + "****************" + idCard.substring(length - 1, length);
        Log.v("LoginTest", "tidCard:" + tidCard);
        return tidCard;
    }

    /**
     * 姓名脱敏
     * @param username
     * @return
     */
    public static String encrypted(String username){
        String maskName="";
        if (!TextUtils.isEmpty(username)) {
            maskName = username.length() > 2 ? username.substring(0, 1) + "*" + username.substring(username.length() - 1, username.length())
                            : "*" + username.substring(username.length() - 1, username.length());
        }
        return maskName;
    }

    /**
     * @param fromIndex 脱敏起始位置
     * @param endIndex  脱敏结束位置
     * @param content   指定字符串
     * @return 对指定字符串进行加密
     */
    public static String encrypted(int fromIndex, int endIndex, String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        if (fromIndex > content.length() || fromIndex < 0) {
            return content;
        }
        if (endIndex > content.length() || endIndex < 0) {
            return content;
        }
        int length = endIndex - fromIndex + 1;
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < length; i++) {
            encrypted.append("*");
        }
        return content.substring(0, fromIndex) + encrypted + content.substring(endIndex + 1, content.length());
    }

    /**
     * @param fromIndex 脱敏起始位置
     * @param endIndex  脱敏结束位置
     * @param content   指定字符串
     * @return 对指定字符串进行加密和添加空格
     */
    public static String encryptedAndSpace(int fromIndex, int endIndex, String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        if (fromIndex > content.length() || fromIndex < 0) {
            return content;
        }
        if (endIndex > content.length() || endIndex < 0) {
            return content;
        }
        int length = endIndex - fromIndex;
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < length; i++) {
            encrypted.append("*");
        }
        return content.substring(0, fromIndex) + " " + encrypted + " " + content.substring(endIndex, content.length());
    }


    /**
     * 判断是否安装了支付宝
     * @param context
     * @return
     */
    public static boolean isAlipayInstalled(Context context) {
        PackageManager manager = context.getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List<ResolveInfo> list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }


}
