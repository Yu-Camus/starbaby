package com.starbaby_03.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 
 * �����ͼ���Զ�ȷ����Ȼ�߶�,ȷ���߶Ȼ������ú͹�ģ�������ߴ�ȡ����ͼ��ߴ�
 * �����ͼ������һ��ImageChangeListener ������Ͳ(���� isEmpty)һ���Ѿ���������ImageView
 * 
 */
public class ScaleImageView extends ImageView {
    private Bitmap currentBitmap;
    private ImageChangeListener imageChangeListener;
    private boolean scaleToWidth = false;  //�����־ȷ���Ƿ�Ӧ�ò����߶��������ֹ��Ŀ�
   
    public ScaleImageView(Context context) {
        super(context);
        init();
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    	//����ͼƬ��ʾ������ CENTER_INSIDE
        this.setScaleType(ScaleType.CENTER_INSIDE);
    }
    //����ͼƬ
    public void recycle() {
        setImageBitmap(null);
        if ((this.currentBitmap == null) || (this.currentBitmap.isRecycled()))
            return;
        this.currentBitmap.recycle();
        this.currentBitmap = null;
    }
    /**
     * ImageView������ʾ��ͼƬ
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        currentBitmap = bm;
        super.setImageBitmap(currentBitmap);
        if (imageChangeListener != null)
            imageChangeListener.changed((currentBitmap == null));
    }

    @Override
    public void setImageDrawable(Drawable d) {
        super.setImageDrawable(d);
        if (imageChangeListener != null)
            imageChangeListener.changed((d == null));
    }

    @Override
    public void setImageResource(int id) {
        super.setImageResource(id);
    }

    public interface ImageChangeListener {
    	//��imageview���ı�ʱ��һ��callback
        void changed(boolean isEmpty);
    }

    public ImageChangeListener getImageChangeListener() {
        return imageChangeListener;
    }

    public void setImageChangeListener(ImageChangeListener imageChangeListener) {
        this.imageChangeListener = imageChangeListener;
    }

    private int imageWidth;
    private int imageHeight;

    public void setImageWidth(int w) {
        imageWidth = w;
    }

    public void setImageHeight(int h) {
        imageHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        /**
         * �����͸߶�Ҫscale ���ȿ��Ǹ��ݿ����ȱ���scaleͼ�δ�С
         */

        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            scaleToWidth = true;
        } else if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
            scaleToWidth = false;
        } else
            throw new IllegalStateException("width or height needs to be set to match_parent or a specific dimension");

        if (imageWidth == 0) {
            // nothing to measure
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        } else {
            if (scaleToWidth) {
                int iw = imageWidth;
                int ih = imageHeight;
                int heightC = width * ih / iw;
                if (height > 0)
                    if (heightC > height) {
                        // dont let hegiht be greater then set max
                        heightC = height;
                        width = heightC * iw / ih;
                    }

                this.setScaleType(ScaleType.CENTER_CROP);
                setMeasuredDimension(width, heightC);

            } else {
                // need to scale to height instead
                int marg = 0;
                if (getParent() != null) {
                    if (getParent().getParent() != null) {
                        marg += ((RelativeLayout) getParent().getParent()).getPaddingTop();
                        marg += ((RelativeLayout) getParent().getParent()).getPaddingBottom();
                    }
                }

                int iw = imageWidth;
                int ih = imageHeight;

                width = height * iw / ih;
                height -= marg;
                setMeasuredDimension(width, height);
            }

        }
    }

}
