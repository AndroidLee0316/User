package com.pasc.lib.userbase.user.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.StringRes;

import com.pasc.lib.widget.dialognt.LoadingDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

/**
 * 用户系统用到的工具类
 */
public class UserUtils {

    /**
     * sp转换为px
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 解析Assets目录下的文件（一行一行读）
     * @param jsonAssetsPath 资源文件对应的路径
     * @return 解析资源文件为String类型
     */
    public static String parseFromAssets(Context context, String jsonAssetsPath) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(jsonAssetsPath)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 解析raw目录下的文件
     * @param context
     * @param rawName   文件名称
     * @return
     */
    public static String parseFromRaw(Context context, int rawName){
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().openRawResource(rawName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String result = "";
            while ((line = bufReader.readLine()) != null){
                result += line;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static LoadingDialog mLoadingDialog;
    private static WeakReference<Activity> activityWeakReference;

    /**
     * 显示loading框
     * @param activity
     */
    public static void showLoading(Activity activity) {
        if (activityWeakReference == null || activityWeakReference.get() != activity) {
            activityWeakReference = new WeakReference<>(activity);
            mLoadingDialog = new LoadingDialog(activityWeakReference.get());
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(activityWeakReference.get());
        }
        mLoadingDialog.setHasContent(false);
        mLoadingDialog.show();
    }

    /**
     * 显示loading框
     * @param activity
     * @param resId 显示加载文字
     */
    public static void showLoading(Activity activity, @StringRes int resId) {
        showLoading(activity, activity.getString(resId));
    }

    /**
     * 显示loading框
     * @param activity
     * @param loadingTip    显示加载文字
     */
    public static void showLoading(Activity activity, String loadingTip) {
        if (activityWeakReference == null || activityWeakReference.get() != activity) {
            activityWeakReference = new WeakReference<>(activity);
            mLoadingDialog = new LoadingDialog(activityWeakReference.get());
        } else if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(activity, loadingTip);
        } else {
            mLoadingDialog.setContent(loadingTip);
        }
        mLoadingDialog.setHasContent(true);
        mLoadingDialog.show();
    }

    /**
     * 隐藏加载框
     */
    public static void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
        if (activityWeakReference != null) {
            activityWeakReference = null;
        }
    }

}
