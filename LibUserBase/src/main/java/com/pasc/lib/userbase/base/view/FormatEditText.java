package com.pasc.lib.userbase.base.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.pasc.lib.widget.toolbar.ClearEditText;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kuangxiangkui192 on 2019/1/3.
 * 输入内容格式化，如手机格式化为344，验证码格式为33格式
 */
public class FormatEditText extends ClearEditText {
    private List<Integer> mSplitPos;
    public static final int TYPE_NONE = -1; //无
    public static final int TYPE_PHONE = 0; //手机号
    public static final int TYPE_SMS_CODE = 1; //验证码
    public static final int TYPE_BANK_NUMBER = 2; //银行卡号
    public static final int TYPE_ID_CARD = 3; //身份证号

    public FormatEditText(Context context) {
        super(context);
    }

    public FormatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FormatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFormatType(int type) {
        if (mSplitPos == null) {
            mSplitPos = new LinkedList<>();
        } else {
            mSplitPos.clear();
        }
        switch (type) {
            case TYPE_PHONE:
                mSplitPos.add(3);
                mSplitPos.add(8);
                break;
            case TYPE_SMS_CODE:
                mSplitPos.add(3);
                break;
            case TYPE_BANK_NUMBER:
                mSplitPos.add(4);
                mSplitPos.add(9);
                mSplitPos.add(14);
                mSplitPos.add(19);
                mSplitPos.add(24);
                break;
            case TYPE_ID_CARD:
                mSplitPos.add(6);
                mSplitPos.add(15);
                break;
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text == null || text.length() == 0) {
            return;
        }

        if (mSplitPos != null && lengthAfter > 0) {
            StringBuilder sb = new StringBuilder(text.toString().replace(" ", ""));
            for (int pos : mSplitPos) {
                if (sb.length() > pos) {
                    sb.insert(pos, ' ');
                }
            }

            if (!text.toString().equals(sb.toString())) {
                int step = sb.length() - text.length();
                step += start + lengthAfter;
                setText(sb);
                if (step >= getText().toString().length()) {
                    setSelection(getText().toString().length());
                } else if (step <= 0) {
                    setSelection(0);
                } else {
                    setSelection(step);
                }
            }
        }
    }

    @Override public boolean onTextContextMenuItem(int id) {
        //粘贴
        if (id == android.R.id.paste) {
            ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            //改变剪贴板中Content
            if (clipboardManager == null) {
                return super.onTextContextMenuItem(id);
            }
            CharSequence text = clipboardManager.getText();

            if (text == null || text.length() == 0) {
                return super.onTextContextMenuItem(id);
            }

            if (mSplitPos != null) {
                StringBuilder sb = new StringBuilder(text.toString().replace(" ", ""));
                for (int pos : mSplitPos) {
                    if (sb.length() > pos) {
                        sb.insert(pos, ' ');
                    }
                }

                if (!text.toString().equals(sb.toString())) {
                    setText(sb);
                    setSelection(getText().toString().length());
                }
            }
        }
        return super.onTextContextMenuItem(id);

    }

    public String getOriginalText() {
        return getText().toString().replace(" ", "");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        super.onFocusChange(v, hasFocus);
        if (onFocusChangeOutListener != null){
            onFocusChangeOutListener.onFocusChange(v,hasFocus);
        }
    }

    public static interface OnFocusChangeOutListener{
        void onFocusChange(View v, boolean hasFocus);
    }

    private OnFocusChangeOutListener onFocusChangeOutListener;

    public void setOnFocusChangeOutListener(OnFocusChangeOutListener onFocusChangeOutListener) {
        this.onFocusChangeOutListener = onFocusChangeOutListener;
    }


}
