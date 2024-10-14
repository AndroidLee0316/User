package com.pasc.business.login.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.pasc.business.login.activity.NewLoginActivity;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.ICallBack;

import java.lang.ref.WeakReference;

/**
 * 使用范例：(需要用到的地方，点击事件里面添加如下判断，如果需要登陆直接return即可)
 */
// case R.id.user_tv_product_details_buy_buy:
//    if (MyLoginUtils.getInstance().isNeedLogin(this,v)) {
//          return;
//    }
//    ..点击事件逻辑...

/**
 * 登陆成功后触发点击动作Login
 * Created by zc on 2017年12月18日11:04:05
 */
public class LoginSuccessActionUtils<T> {
    private volatile static LoginSuccessActionUtils instance = null;
    //弱引用自定义view 防止内存泄露
    private WeakReference<T> tWeakRef;
    //亲测 callback 用弱引用，极容易被回收调，所以针对性处理
    private ICallBack callBack;
    //调用处activity的名字（用于及时清空回调逻辑）
    private String activityName;

    private LoginSuccessActionUtils() {
    }

    public static LoginSuccessActionUtils getInstance() {
        if (instance == null) {
            synchronized (LoginSuccessActionUtils.class) {
                if (instance == null) {
                    instance = new LoginSuccessActionUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 是否需要登陆
     *
     * @param t 需要回调点击事件的view
     * @return true 需要
     */
    public boolean isNeedLogin(Context context, T t) {
        context = checkContext(context);
        if (isHasLogin(context)) {
            return false;
        } else {
            goToLogin(context, t);
            return true;
        }
    }

    /**
     * 是否已经登录
     */
    public boolean isHasLogin(Context context) {
        return AppProxy.getInstance().getUserManager().isLogin();
    }

    /**
     * 带接口的回调 （回调内容写在接口实现里面）
     */
    public void checkLoginWithCallBack(Context context, final T callBack) {
        context = checkContext(context);
        if (isHasLogin(context)) {
            if (callBack != null && callBack instanceof ICallBack) {//已经登录的情况 直接回调
                ((ICallBack) callBack).callBack();
            }
      /*
      final Context finalContext = context;
      UserBiz.checkToken().subscribe(new BaseRespObserver<TokenStatusResp>() {
        public void onError(String msg) {
          CommonUtils.toastMsg(msg);
        }

        public void onSuccess(TokenStatusResp tokenStatusResp) {
          super.onSuccess(tokenStatusResp);
          if (tokenStatusResp.isValid()) {
            if (callBack != null && callBack instanceof ICallBack) {//已经登录的情况 直接回调
              ((ICallBack) callBack).callBack();
            }
          } else {
            goToLogin(finalContext, callBack);
          }
        }
      });
      */
        } else {
            goToLogin(context, callBack);
        }
    }

    /**
     * 带接口的回调 （回调内容写在接口实现里面）
     */
    public void checkLoginWithCallBack4Recommend(Context context, T callBack, String path) {
        context = checkContext(context);
        if (isHasLogin(context)) {
            if (callBack != null && callBack instanceof ICallBack) {//已经登录的情况 直接回调
                ((ICallBack) callBack).callBack();
            }
        } else {
            goToLogin(context, callBack, path);
        }
    }

    /**
     * 登陆
     *
     * @param t 需要回调点击事件的view
     */
    public void goToLogin(Context context, T t) {
        context = checkContext(context);
        if (t instanceof ICallBack) {
            callBack = (ICallBack) t;
        } else {
            this.tWeakRef = new WeakReference<>(t);
        }
        if (context instanceof Application) {
            Intent intent =
                    new Intent(context.getApplicationContext(), NewLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(intent);
        } else {
            context.startActivity(new Intent(context, NewLoginActivity.class));
        }
    }

    /**
     * 登陆
     *
     * @param t 需要回调点击事件的view
     */
    public void goToLogin(Context context, T t, String path) {
        context = checkContext(context);
        if (t instanceof ICallBack) {
            callBack = (ICallBack) t;
        } else {
            this.tWeakRef = new WeakReference<>(t);
        }
        Intent intent = new Intent(context, NewLoginActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent.putExtra("path", path);
        }
        context.startActivity(intent);
    }

    /**
     * 登陆之后的动作
     */
    public void onLoginSuccessAction() {
        if (tWeakRef != null && tWeakRef.get() != null) {
            if (tWeakRef.get() instanceof View) {
                View view = (View) (tWeakRef.get());
                if (view.getContext() != null/**/
                        && view.getContext() instanceof NewLoginActivity) {//防止记录记录页面的控件，导致重复触发
                    tWeakRef = null;
                    return;
                }
                view.performClick();
            }
            tWeakRef = null;
        } else {
            if (callBack != null) {
                callBack.callBack();
            }
            callBack = null;
        }
    }

    /**
     * NewLoginActivity finish 时候调用这个方法清空view 或者其他callback
     */
    public void clearCallback() {
        tWeakRef = null;
        callBack = null;
    }

    /**
     * 基类baseActivity onDestroy 回调的方法（判断如果当前finish得activity如何和纪录事件的activity是同一个activity时清空事件）
     */
    public void onDestroyClearCallback(Context context) {
        if (context != null && !TextUtils.isEmpty(activityName) && activityName.equals(
                context.getClass().getName())) {
            Log.d("LoginSuccessActionUtils", "activity已经被finish，清空回调事件   current:"
                    + context.getClass().getName()
                    + "  last:"
                    + activityName);
            clearCallback();
        }
    }

    /**
     * 是否有记录的事件（是否有下一步的点击回调动作）
     */
    public boolean isHasNextStep() {
        return (tWeakRef != null && tWeakRef.get() != null) || callBack != null;
    }

    private Context checkContext(Context context) {
        activityName = context != null ? context.getClass().getCanonicalName() : null;
        return context == null ? AppProxy.getInstance().getContext() : context;
    }
}
