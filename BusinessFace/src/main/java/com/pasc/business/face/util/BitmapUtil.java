package com.pasc.business.face.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * description ：Bitmap处理工具类
 */
public class BitmapUtil {

    /**
     * 图片byte数组转为Bitmap对象
     *
     * @param b 图片Byte数组
     * @return Bitmap对象
     */
    public static Bitmap bytes2Bitmap(byte[] b) {
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

}