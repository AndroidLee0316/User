package com.pasc.business.face.view;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.pasc.lib.base.util.CameraUtils;
import com.pasc.lib.base.util.ScreenUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhanqiang545 on 18/1/9.
 */
@SuppressWarnings("deprecation")
public class CameraSurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private Context mContext;
    private Camera mCamera;
    private Camera.Parameters mCameraParameters;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private int mWidth;
    private int mHeight;
    private int mCameraDisplayOrientation;
    private int mCameraMode = Camera.CameraInfo.CAMERA_FACING_FRONT;

    public void initPreview(Context context, FrameLayout frameLayout, int cameraMode) {
        mContext = context;
        mCameraMode = cameraMode;
        if (mSurfaceView == null) {
            WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            this.mSurfaceView = new SurfaceView(mContext);
            mHolder = this.mSurfaceView.getHolder();
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            ViewGroup.LayoutParams layoutParams1 = frameLayout.getLayoutParams();
            layoutParams1.width = (int) (ScreenUtils.getScreenWidth() * 0.72f);
            layoutParams1.height = (int) (layoutParams1.width * 4.0f / 3);
            frameLayout.setLayoutParams(layoutParams1);
            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT);//宽高比跟预览分辨率比一样，所以480*640分辨率时，宽高比为3:4
            this.mSurfaceView.setLayoutParams(layoutParams);
            frameLayout.removeAllViews();
            frameLayout.addView(this.mSurfaceView);
        }
    }

    public void openCamera() {
        mHolder.addCallback(this);
        if (mCamera != null) {
            releaseCamera();
        }
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraInfo.facing == mCameraMode) {
                mCamera = Camera.open(i);
                break;
            } else if (cameraCount == 1) {
                mCamera = Camera.open(i);
                mCameraMode = i;
            }
        }

        mCameraParameters = mCamera.getParameters();
        //获取支持的格式
        Camera.Size previewSize =
                CameraUtils.getPropPreviewSize(mCameraParameters.getSupportedPreviewSizes(),
                        480, 640);
        mWidth = previewSize.width;
        mHeight = previewSize.height;
        //格式要求,安卓默认格式
        mCameraParameters.setPreviewFormat(ImageFormat.NV21);
        //建议预览尺寸为640*480，其它尺寸效果没有这么好
        mCameraParameters.setPreviewSize(mWidth, mHeight);
        List<String> focusModes = mCameraParameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_FIXED)) {
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_INFINITY)) {
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        } else {
            mCameraParameters.setFocusMode(focusModes.get(0));
        }
        mCameraParameters.setPreviewFrameRate(30);
        mCamera.setParameters(mCameraParameters);
    }

    public void startPreview() {
        if (mCamera != null) {
            try {
                mCameraDisplayOrientation = CameraUtils.getCameraDisplayOrientation(mContext, mCameraMode);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.setDisplayOrientation(mCameraDisplayOrientation);
                mCamera.setPreviewCallback(this);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
        }
    }

    private void releaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        releaseCamera();
        if (mContext != null) {
            mContext = null;
        }
        if (mPreviewCallback != null) {
            mPreviewCallback = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            stopPreview();
            startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreview();
        releaseCamera();
        mHolder.removeCallback(this);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mPreviewCallback != null) {
            mPreviewCallback.onPreviewFrame(data);
        }
        Log.d("tag", "onPreviewFrame: ");
    }

    private PreviewCallback mPreviewCallback;

    public void setPreviewCallback(PreviewCallback onFrameDataCallback) {
        mPreviewCallback = onFrameDataCallback;
    }

    public int getCameraOri() {
        return mCameraDisplayOrientation;
    }

    public int getCameraWidth() {
        return mWidth;
    }

    public int getCameraHeight() {
        return mHeight;
    }

    public int getCameraMode() {
        return mCameraMode;
    }

    public boolean available() {
        return mCamera != null;
    }

    public interface PreviewCallback {

        void onPreviewFrame(byte[] frameData);
    }
}
