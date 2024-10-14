package com.pingan.smt.template.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.pasc.business.login.wechat.R;
import com.pasc.business.login.wechat.WechatAuthOutListener;
import com.pasc.lib.base.event.BaseEvent;
import com.pasc.lib.share.ShareManager;
import com.pasc.lib.userbase.base.event.EventKey;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    public IWXAPI mWxApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_entry_activity);
        //拿到微信appId
        String wechatAppId = ShareManager.getInstance().getAppSecretConfig().getWechatAppId();
        mWxApi = WXAPIFactory.createWXAPI(this, wechatAppId, false);
        mWxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }


    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                switch (baseResp.getType()) {
                    case ConstantsAPI.COMMAND_SENDAUTH:
                        String state = ((SendAuth.Resp) baseResp).state;
                        if (state.equals(ShareManager.SEND_AUTH_REQ_STATE)) {//微信授权
                            //微信授权成功，处理成功逻辑

                            //登录回调,获得CODE
                            String code = ((SendAuth.Resp) baseResp).code;
                            WechatAuthOutListener.getInstance().onWechatAuthSuccess(code);
                        } else if (state.equals(ShareManager.SEND_BIND_REQ_STATE)) {//绑定到微信
                            //微信绑定成功，处理成功逻辑

                            //登录回调,获得CODE
                            String code = ((SendAuth.Resp) baseResp).code;
                        } else {
                            //微信授权失败，处理失败逻辑
                        }
                        break;
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        //微信分享成功，处理微信分享成功逻辑
                        break;
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                switch (baseResp.getType()) {
                    case ConstantsAPI.COMMAND_SENDAUTH:
                        //微信授权取消回调，处理取消逻辑
                        WechatAuthOutListener.getInstance().onWechatAuthFailed(String.valueOf(BaseResp.ErrCode.ERR_USER_CANCEL),"取消授权");
                        break;
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        //微信分享取消回调，处理取消逻辑
                        break;
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                switch (baseResp.getType()) {
                    case ConstantsAPI.COMMAND_SENDAUTH:
                        //微信授权失败回调，处理失败逻辑
                        if (TextUtils.isEmpty(baseResp.errStr)){
                            baseResp.errStr = "授权失败";
                        }
                        WechatAuthOutListener.getInstance().onWechatAuthFailed(String.valueOf(baseResp.errCode),baseResp.errStr);
                        break;
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        //微信分享失败回调，处理失败逻辑
                        break;
                }
                break;
            default:
                break;
        }
        finish();
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }
}