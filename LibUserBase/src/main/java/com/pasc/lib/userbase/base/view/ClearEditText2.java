package com.pasc.lib.userbase.base.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.pasc.lib.widget.toolbar.ClearEditText;

/**
 * 功能：
 *
 * @author lichangbao702
 * @email : lichangbao702@pingan.com.cn
 * @date : 2020/2/26
 */
public class ClearEditText2 extends ClearEditText{

    public ClearEditText2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void updateClearIconVisiable(boolean hasFocus){
        if (hasFocus && getEditableText().toString().length()>0){
            Drawable right = mClearDrawable;
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], right, this.getCompoundDrawables()[3]);

        }else {
            Drawable right = null;
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], right, this.getCompoundDrawables()[3]);
        }
    }


}
