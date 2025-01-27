package com.pasc.business.user.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pasc.business.user.R;

import org.w3c.dom.Attr;

import java.util.logging.Logger;

/**
 * Description：
 * Author：Administrator
 * Date：2019/2/11
 */
public class VerificationCodeEditText extends FrameLayout {
    private String TAG = "VerificationCodeEditText";

    private Context context;

    private LinearLayout llCodeMain;
    private EditText editCodeNum;
    private TextView tvCode0,tvCode1,tvCode2,tvCode3;
    private ImageView imgCode0,imgCode1,imgCode2,imgCode3;

    private int codeTextColor = 0xFF000000;//文字颜色默认黑色
    private int codeTextSize = 14;//文字默认14号字
    private int codeTextBackgroud = R.drawable.bg_border_bottom_gold;//每个框默认背景样式
    private int codeCursorImg = R.drawable.guangbiao;//默认光标
    private int codeCursorHeight = 30 ; //光标图片默认高度

    //监听
    public interface OnInputListener {
        //输入监听
        void OnEdittextChange(CharSequence charSequence, int i, int i1, int i2);
        //输入完成监听
        void OnInputOk(String codeNum);
    }

    private OnInputListener onInputListener;

    public void setOnInputListener(OnInputListener onInputListener){
        this.onInputListener = onInputListener;
    }


    public VerificationCodeEditText(Context context) {
        super(context);
        this.context = context;
    }

    public VerificationCodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        loadView(attrs);
    }

    public VerificationCodeEditText(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private void loadView(AttributeSet attrs){
        View view = LayoutInflater.from(context).inflate(R.layout.edittext_verification_code,this);

        //点击后先让输入框获取焦点，之后弹出软键盘，输入时进行各种判断
        //页面分两层，一层是显示输入的验证码，另一层是一个输入框，放已经输入的内容，

        initView(view,attrs);
        initEvent();

    }

    private void initView(View view,AttributeSet attrs){
        llCodeMain = view.findViewById(R.id.user_ll_codeMain);

        editCodeNum = view.findViewById(R.id.user_edit_codeNum);

        tvCode0 = view.findViewById(R.id.user_tuser_code0);
        tvCode1 = view.findViewById(R.id.user_tuser_code1);
        tvCode2 = view.findViewById(R.id.user_tuser_code2);
        tvCode3 = view.findViewById(R.id.user_tuser_code3);

        imgCode0 = view.findViewById(R.id.user_img_code0);
        imgCode1 = view.findViewById(R.id.user_img_code1);
        imgCode2 = view.findViewById(R.id.user_img_code2);
        imgCode3 = view.findViewById(R.id.user_img_code3);


        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.user_code_edittext);
        codeTextColor = typedArray.getColor(R.styleable.user_code_edittext_user_codeTextColor,codeTextColor);
        codeTextSize = typedArray.getDimensionPixelSize(R.styleable.user_code_edittext_user_codeTextSize,codeTextSize);
        codeTextBackgroud = typedArray.getResourceId(R.styleable.user_code_edittext_user_codeTextBackgroud,codeTextBackgroud);
        codeCursorImg = typedArray.getResourceId(R.styleable.user_code_edittext_user_codeCursorImg,codeCursorImg);
        codeCursorHeight =typedArray.getDimensionPixelSize(R.styleable.user_code_edittext_user_codeCursorHeight,codeCursorHeight);
        typedArray.recycle();

        Log.d(TAG,codeCursorImg+"");

        //字体颜色
        tvCode0.setTextColor(codeTextColor);
        tvCode1.setTextColor(codeTextColor);
        tvCode2.setTextColor(codeTextColor);
        tvCode3.setTextColor(codeTextColor);

        //字体大小
        tvCode0.setTextSize(codeTextSize);
        tvCode1.setTextSize(codeTextSize);
        tvCode2.setTextSize(codeTextSize);
        tvCode3.setTextSize(codeTextSize);

        //输入框背景
        tvCode0.setBackgroundResource(codeTextBackgroud);
        tvCode1.setBackgroundResource(codeTextBackgroud);
        tvCode2.setBackgroundResource(codeTextBackgroud);
        tvCode3.setBackgroundResource(codeTextBackgroud);

        //光标高度
        LayoutParams linearParams0 = (LayoutParams) imgCode0.getLayoutParams();
        linearParams0.height = codeCursorHeight;
        imgCode0.setLayoutParams(linearParams0);
        LayoutParams linearParams1 = (LayoutParams) imgCode1.getLayoutParams();
        linearParams1.height = codeCursorHeight;
        imgCode1.setLayoutParams(linearParams1);
        LayoutParams linearParams2 = (LayoutParams) imgCode2.getLayoutParams();
        linearParams2.height = codeCursorHeight;
        imgCode2.setLayoutParams(linearParams2);
        LayoutParams linearParams3 = (LayoutParams) imgCode3.getLayoutParams();
        linearParams3.height = codeCursorHeight;
        imgCode3.setLayoutParams(linearParams3);

        //光标图片
        Glide.with(this).load(codeCursorImg).into(imgCode0);
        Glide.with(this).load(codeCursorImg).into(imgCode1);
        Glide.with(this).load(codeCursorImg).into(imgCode2);
        Glide.with(this).load(codeCursorImg).into(imgCode3);


    }

    private void initEvent(){
        //点击控件，获取焦点
        llCodeMain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                editCodeNum.setFocusable(true);
                editCodeNum.setFocusableInTouchMode(true);
                editCodeNum.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editCodeNum, 0);
            }
        });


        //输入监听
        editCodeNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0){
                    Log.d(TAG,charSequence.toString());

                    String stInput = charSequence.toString();

                    //定义数组，把输入的内容拆开一个个放
                    char[] stCode = new char[]{4};
                    stCode = stInput.toCharArray();

                    switch (stInput.length()){
                        case 1:
                            if (stCode[0] + "" != null){
                                //显示当前的内容
                                tvCode0.setText(stCode[0] + "");
                                //清空右边一位的内容
                                tvCode1.setText("");

                                //显示后边一位的点
                                imgCode1.setVisibility(View.VISIBLE);
                                //隐藏当前位置的点
                                imgCode0.setVisibility(View.INVISIBLE);
                                //隐藏后边两位的店
                                imgCode2.setVisibility(View.INVISIBLE);

                            }
                            break;
                        case 2:
                            if (stCode[1] + "" != null){
                                //显示当前的内容
                                tvCode1.setText(stCode[1] + "");
                                //清空右边一位的内容
                                tvCode2.setText("");


                                //显示后边一位的点
                                imgCode2.setVisibility(View.VISIBLE);
                                //隐藏当前位置的点
                                imgCode1.setVisibility(View.INVISIBLE);
                                //隐藏后边两位的店
                                imgCode3.setVisibility(View.INVISIBLE);

                            }
                            break;
                        case 3:
                            if (stCode[2] + "" != null){
                                //显示当前的内容
                                tvCode2.setText(stCode[2] + "");
                                //清空右边一位的内容
                                tvCode3.setText("");


                                //显示后边一位的点
                                imgCode3.setVisibility(View.VISIBLE);
                                //隐藏当前位置的点
                                imgCode2.setVisibility(View.INVISIBLE);

                            }
                            break;
                        case 4:
                            if (stCode[3] + "" != null){
                                //显示当前的内容
                                tvCode3.setText(stCode[3] + "");
                                //清空右边一位的内容

                                //隐藏当前位置的点
                                imgCode3.setVisibility(View.INVISIBLE);

                            }
                            break;
                    }

                }else {

                    tvCode0.setText("");

                    //显示第0位的点
                    imgCode0.setVisibility(View.VISIBLE);
                    //隐藏位的店
                    imgCode1.setVisibility(View.INVISIBLE);
                }

                if (onInputListener!=null){
                    onInputListener.OnEdittextChange(charSequence,i,i1,i2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //通过输入框得失焦点来显示假的光标
        editCodeNum.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
//                    Log.d(TAG,"得到焦点"+editCodeNum.getText().toString().length());

                    String stInput = editCodeNum.getText().toString();

                    switch (stInput.length()){
                        case 0:
                            imgCode0.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            imgCode1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            imgCode2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            imgCode3.setVisibility(View.VISIBLE);
                            break;
                    }

                } else {
                    // 此处为失去焦点时的处理内容
//                    Log.d(TAG,"失去焦点");

                    imgCode0.setVisibility(View.INVISIBLE);
                    imgCode1.setVisibility(View.INVISIBLE);
                    imgCode2.setVisibility(View.INVISIBLE);
                    imgCode3.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    public String getCode(){
        return editCodeNum.getText().toString();
    }


}
