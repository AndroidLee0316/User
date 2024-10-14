package com.pasc.business.login.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import java.lang.reflect.Method;

/**
 * Created by zhangxu678 on 2018/9/25.
 */
public class NavigationBarUtils {

  public static void assistActivity(View content, RelativeLayout targetView) {
    new NavigationBarUtils(content, targetView);
  }

  private View mChildOfContent;
  private int usableHeightPrevious;
  private ViewGroup.LayoutParams targetLayoutParams;
  private RelativeLayout targetView;

  private NavigationBarUtils(View content, RelativeLayout targetView) {
    mChildOfContent = content;
    this.targetView = targetView;
    targetView.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          public void onGlobalLayout() {
            possiblyResizeChildOfContent();
          }
        });
    targetLayoutParams = targetView.getLayoutParams();
  }

  private void possiblyResizeChildOfContent() {
    int usableHeightNow = computeUsableHeight();
    if (usableHeightNow != usableHeightPrevious) {
      ViewGroup.MarginLayoutParams marginParams;
      if (targetLayoutParams instanceof ViewGroup.MarginLayoutParams) {
        marginParams = (ViewGroup.MarginLayoutParams) targetLayoutParams;
      } else {
        //不存在时创建一个新的参数
        marginParams = new ViewGroup.MarginLayoutParams(targetLayoutParams);
      }
      marginParams.setMargins(marginParams.leftMargin, marginParams.topMargin / 2,
          marginParams.rightMargin, marginParams.bottomMargin);
      targetView.setLayoutParams(marginParams);
      targetView.requestLayout();
      usableHeightPrevious = usableHeightNow;
    }
  }

  private int computeUsableHeight() {
    Rect r = new Rect();
    mChildOfContent.getWindowVisibleDisplayFrame(r);
    return (r.bottom);
  }

  public static boolean checkDeviceHasNavigationBar(Context context) {
    boolean hasNavigationBar = false;
    Resources rs = context.getResources();
    int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
    if (id > 0) {
      hasNavigationBar = rs.getBoolean(id);
    }
    try {
      Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
      Method m = systemPropertiesClass.getMethod("get", String.class);
      String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
      if ("1".equals(navBarOverride)) {
        hasNavigationBar = false;
      } else if ("0".equals(navBarOverride)) {
        hasNavigationBar = true;
      }
    } catch (Exception e) {

    }
    return hasNavigationBar;
  }
}


