package com.ceabie.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * 水平布局的Drawable.
 *
 * @author chenxi
 */
public class HorizontalDrawable extends Drawable {
    public static final int MODE_WITH_CONTAINER = 0;
    public static final int MODE_INTRINSIC = 1;
    public static final int MODE_WRAP = 2;

    private Drawable[] mDrawables;
    private int mIntrinsicWidth;
    private int mIntrinsicHeight;

    private int mSpacing;

    /**
     * Instantiates a new Horizontal drawable.
     *
     * @param drawables drawable元素
     * @param spacing   间距
     * @param boundMode 尺寸模式
     */
    public HorizontalDrawable(Drawable[] drawables, int spacing, int boundMode) {
        mSpacing = spacing;
        setDrawables(drawables, boundMode);
    }

    /**
     * 设置间距
     *
     * @param spacing 间距
     * @author chenxi
     */
    public void setHorizontalSpacing(int spacing) {
        mSpacing = spacing;
        resizeDrawables(MODE_WITH_CONTAINER);
    }


    /**
     * Sets drawables.
     *
     * @param drawables drawable元素
     * @param boundMode 尺寸模式
     * @author chenxi
     */
    public void setDrawables(Drawable[] drawables, int boundMode) {
        mDrawables = drawables;

        if (boundMode != MODE_WITH_CONTAINER) {
            resizeDrawables(boundMode);
        }

    }

    private void resizeDrawables(int boundMode) {
        Rect rect = getBounds();
        int boundWidth = 0;
        int boundHeight = 0;
        mIntrinsicWidth = 0;
        mIntrinsicHeight = 0;

        if (mDrawables != null) {
            int width;
            int height = rect.height();
            int length = mDrawables.length;
            for (int i = 0; i < length; i++) {
                Drawable drawable = mDrawables[i];
                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();

                mIntrinsicWidth += intrinsicWidth;
                mIntrinsicHeight = Math.max(mIntrinsicHeight, height);

                if (boundMode == MODE_WRAP) {
                    Rect bounds = drawable.getBounds();
                    width = bounds.width();
                    height = bounds.height();
                } else if (boundMode == MODE_INTRINSIC) {
                    width = intrinsicWidth;
                    height = intrinsicHeight;
                } else if (intrinsicHeight != 0) {
                    width = (int) ((intrinsicWidth / (float) intrinsicHeight) * height);
                } else {
                    width = 0;
                }

                drawable.setBounds(0, 0, width, height);

                boundHeight = Math.max(boundHeight, height);
                boundWidth += width;
                if (mSpacing > 0 && (i < length - 1)) {
                    mIntrinsicWidth += mSpacing;
                    boundWidth += mSpacing;
                }
            }

            if (boundMode == MODE_WRAP) {
                rect.set(rect.left, rect.top, boundWidth, boundHeight);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDrawables != null) {
            canvas.save(Canvas.ALL_SAVE_FLAG);
            Rect rect = getBounds();
            canvas.translate(rect.left, rect.top);

            for (int i = 0; i < mDrawables.length; i++) {
                Drawable drawable = mDrawables[i];

                drawable.draw(canvas);
                Rect bounds = drawable.getBounds();
                canvas.translate((bounds.width()) + mSpacing, 0);
            }

            canvas.restore();
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getIntrinsicWidth() {
        return mIntrinsicWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mIntrinsicHeight;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        resizeDrawables(MODE_WITH_CONTAINER);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
