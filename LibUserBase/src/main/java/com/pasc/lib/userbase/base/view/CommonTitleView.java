package com.pasc.lib.userbase.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pasc.lib.userbase.R;


/**
 * 通用Title View
 * Created by duyuan797 on 17/3/16.
 */

public class CommonTitleView extends LinearLayout {

    private static final String TAG = CommonTitleView.class.getSimpleName();

    protected View view;

    protected ImageView mLeftIv;

    protected TextView mRightTV; // 返回

    protected TextView mTitleTV; // 标题

    protected TextView mLeftTV; // 下一个
    protected ImageView mRightIv;  //右图标
    protected ImageView mRightLeftIv;

    private View topView;

    private View underLine;

    private ProgressBar mProgressBar;

    private RelativeLayout rlFirst;

    private TranslateAnimation mShowAction, mHiddenAction;

    public CommonTitleView(Context context) {
        super(context);
        initView(context, null);
    }

    public CommonTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {
        // 设置背景
        setBackgroundColor(getResources().getColor(R.color.title_bar));

        view = LayoutInflater.from(context).inflate(R.layout.userbase_common_title_view, null);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(view, params);

        mRightTV = (TextView) view.findViewById(R.id.common_title_right);
        mTitleTV = (TextView) view.findViewById(R.id.temp_common_title_name);
        mLeftTV = (TextView) view.findViewById(R.id.temp_common_title_left);
        mLeftIv = (ImageView) view.findViewById(R.id.iv_title_left);
        mProgressBar = (ProgressBar) view.findViewById(R.id.temp_progress_bar);
        mRightIv = (ImageView) view.findViewById(R.id.temp_iv_title_Right);
        mRightLeftIv = (ImageView) view.findViewById(R.id.temp_iv_title_right_left);
        rlFirst = (RelativeLayout) view.findViewById(R.id.rl_first);
        underLine = (View) view.findViewById(R.id.temp_view_under_line);
        if (attrs != null) {

            //获得这个控件对应的属性。
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CommonTitleView);

            try {
                //获得属性值
                //getColor(R.styleable.commonTitle_RxBackground, getResources().getColor(R.color.transparent))
                String title = a.getString(R.styleable.CommonTitleView_title);//标题
                int titleColor = a.getColor(R.styleable.CommonTitleView_titleColor, getResources().getColor(R.color.black_333333));//标题颜色
                int bgColor = a.getColor(R.styleable.CommonTitleView_backgroundColor,getResources().getColor( R.color.white_ffffff));//标题颜色
                setBackgroundColor(bgColor);
                int titleSize = a.getDimensionPixelSize(R.styleable.CommonTitleView_titleSize, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics()));
                boolean titleVisibility = a.getBoolean(R.styleable.CommonTitleView_titleVisibility, true);
                mTitleTV.setText(title);
                mTitleTV.setTextColor(titleColor);
                mTitleTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
                mTitleTV.setVisibility(titleVisibility ? VISIBLE : GONE);

                int leftIcon = a.getResourceId(R.styleable.CommonTitleView_leftIcon, R.mipmap.userbase_ic_back);//左边图标
                boolean leftIconVisibility = a.getBoolean(R.styleable.CommonTitleView_leftIconVisibility, true);//左边图标是否显示
                mLeftIv.setImageResource(leftIcon);
                mLeftIv.setVisibility(leftIconVisibility ? VISIBLE : GONE);

                int rightIcon = a.getResourceId(R.styleable.CommonTitleView_rightIcon, R.mipmap.userbase_ic_more_black);//右边图标
                boolean rightIconVisibility = a.getBoolean(R.styleable.CommonTitleView_rightIconVisibility, false);//右边图标是否显示
                mRightIv.setImageResource(rightIcon);
                mRightIv.setVisibility(rightIconVisibility ? VISIBLE : GONE);


                String leftText = a.getString(R.styleable.CommonTitleView_leftText);
                int leftTextColor = a.getColor(R.styleable.CommonTitleView_leftTextColor, getResources().getColor(R.color.black_333333));//左边字体颜色
                int leftTextSize = a.getDimensionPixelSize(R.styleable.CommonTitleView_leftTextSize, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP,16, getResources().getDisplayMetrics()));//标题字体大小
                boolean leftTextVisibility = a.getBoolean(R.styleable.CommonTitleView_leftTextVisibility, false);
                mLeftTV.setText(leftText);
                mLeftTV.setTextColor(leftTextColor);
                mLeftTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
                mLeftTV.setVisibility(leftTextVisibility ? VISIBLE : GONE);

                String rightText = a.getString(R.styleable.CommonTitleView_rightText);
                int rightTextColor = a.getColor(R.styleable.CommonTitleView_rightTextColor, getResources().getColor(R.color.black_333333));//右边字体颜色
                int rightTextSize = a.getDimensionPixelSize(R.styleable.CommonTitleView_rightTextSize, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP,16, getResources().getDisplayMetrics()));//标题字体大小
                boolean rightTextVisibility = a.getBoolean(R.styleable.CommonTitleView_rightTextVisibility, false);
                mRightTV.setText(rightText);
                mRightTV.setTextColor(rightTextColor);
                mRightTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
                mRightTV.setVisibility(rightTextVisibility ? VISIBLE : GONE);

            } finally {
                //回收这个对象
                a.recycle();
            }
        }

        mShowAction =
                new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        mHiddenAction =
                new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(500);
    }

    public CommonTitleView setTopRelBackGround(int resId) {
        rlFirst.setBackgroundResource(resId);
        return this;
    }

    public CommonTitleView setOnLeftClickListener(OnClickListener clickListener) {
        mLeftIv.setOnClickListener(clickListener);
        return this;
    }

    public CommonTitleView setOnLeftTextClickListener(OnClickListener clickListener) {
        mLeftTV.setOnClickListener(clickListener);
        return this;
    }

    public CommonTitleView setOnTitleClickListener(OnClickListener clickListener) {
        mTitleTV.setOnClickListener(clickListener);
        return this;
    }

    public CommonTitleView setOnRightClickListener(OnClickListener clickListener) {
        mRightTV.setOnClickListener(clickListener);
        return this;
    }

    public CommonTitleView setOnRightImageClickListener(OnClickListener clickListener) {
        mRightIv.setOnClickListener(clickListener);
        return this;
    }

    public CommonTitleView setOnRightLeftImageDrawable(int resId) {
        mRightLeftIv.setVisibility(VISIBLE);
        mRightLeftIv.setImageResource(resId);
        return this;
    }

    public CommonTitleView setOnRightLeftImageClickListener(OnClickListener clickListener) {
        mRightLeftIv.setOnClickListener(clickListener);
        return this;
    }

    public CommonTitleView setOnRightLeftImageVisible(int visible) {
        mRightLeftIv.setVisibility(visible);
        return this;
    }

    public CommonTitleView setLeftText(String text) {
        mLeftTV.setVisibility(View.VISIBLE);
        mLeftTV.setText(text);
        mLeftTV.setVisibility(View.VISIBLE);
        return this;
    }

    public CommonTitleView setBgColor(@ColorInt int colorId) {
        rlFirst.setBackgroundColor(colorId);
        return this;
    }

    public void setLeftTextColor(int color) {
        mLeftTV.setTextColor(color);
    }

    public CommonTitleView setLeftText(int text) {
        mLeftTV.setVisibility(View.VISIBLE);
        mLeftTV.setText(text);
        return this;
    }

    public CommonTitleView setTitleText(String text) {
        mTitleTV.setText(text);
        return this;
    }

    public void setTitleTextColor(int color) {
        mTitleTV.setTextColor(color);
    }

    public void setTitleWeight(float weight) {
        LayoutParams params =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.weight = weight;
        params.gravity = Gravity.CENTER_VERTICAL;
        mTitleTV.setLayoutParams(params);
    }

    public CommonTitleView setUnderLineVisible(boolean isVisible){
        if (isVisible){
            underLine.setVisibility(View.VISIBLE);
        }else {
            underLine.setVisibility(View.GONE);
        }
        return this;
    }

    public CommonTitleView setTitleText(int text) {
        mTitleTV.setText(text);
        return this;
    }

    public CommonTitleView setRightText(String text) {
        mRightTV.setVisibility(View.VISIBLE);
        mRightTV.setText(text);
        return this;
    }

    public CommonTitleView setRightText(int text) {
        mRightTV.setVisibility(View.VISIBLE);
        mRightTV.setText(text);
        return this;
    }

    public void setRightTextColor(int rid) {
        mRightTV.setTextColor(rid);
    }

    public void setRightTextSize(int size) {
        mRightTV.setTextSize(size);
    }

    public CommonTitleView setRightTextVisibility(int visiable) {
        mRightTV.setVisibility(visiable);
        return this;
    }

    public void setLeftTextVisibility(int visiable) {
        mLeftTV.setVisibility(visiable);
    }

    public CommonTitleView setBackDrawableLeft(int rId) {
        if (rId != 0) {
            mLeftIv.setVisibility(View.VISIBLE);
            mLeftIv.setImageResource(rId);
        }
        return this;
    }

    public CommonTitleView setBackDrawableVisible(int visible) {
        mLeftIv.setVisibility(visible);
        return this;
    }

    public CommonTitleView setBackDrawableLeftBackground(int rId) {
        if (rId != 0) {
            mLeftIv.setVisibility(View.VISIBLE);
            mLeftIv.setBackgroundResource(rId);
        }
        return this;
    }


    public CommonTitleView setRightImageVisible(int visible) {
        mRightIv.setVisibility(visible);
        return this;
    }

    public CommonTitleView setRightDrawableRight(int rId) {
        if (rId != 0) {
            mRightIv.setVisibility(View.VISIBLE);
            mRightIv.setImageResource(rId);
        }
        return this;
    }

    public void setTitleDrawableRight(int rId) {
        if (rId > 0) {
            Drawable drawable = getResources().getDrawable(rId);
            mTitleTV.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        } else {
            mTitleTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    public void setNextDrawableRight(int rId) {
        if (rId != 0) {
            mRightTV.setVisibility(View.VISIBLE);
            Drawable drawable = getResources().getDrawable(rId);
            mRightTV.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        } else {
            mTitleTV.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void dismissLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    public ImageView getLeftIv() {
        return mLeftIv;
    }

    public TextView getRightTV() {
        return mRightTV;
    }

    public TextView getTitleTV() {
        return mTitleTV;
    }

    public TextView getLeftTV() {
        return mLeftTV;
    }

    public ImageView getRightIv() {
        return mRightIv;
    }
}
