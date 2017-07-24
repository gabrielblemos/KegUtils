package com.keg.kegutils.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.keg.kegutils.R;
import com.keg.kegutils.Utils.UtilsImagem;
import com.keg.kegutils.Utils.VolleyRequest;

/**
 * Created by gamer on 21/01/2017.
 */

public class ImageLoaderView extends View implements View.OnClickListener {

    protected final int NO_ASSET = R.drawable.image_loader_no_asset;
    protected final int ERR_ASSET = R.drawable.image_loader_error_asset;

    protected String url;
    protected boolean rounded;
    protected int idLoadingImage;

    protected Bitmap imageBitmap;
    protected boolean hasLoadingError;
    protected AnimationDrawable frameAnimation;
    protected OnClickListener otherOnClickListener;

    public ImageLoaderView(Context context) {
        super(context);
        initAttributeSet(null, 0);
    }

    public ImageLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(attrs, 0);
    }

    public ImageLoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(attrs, defStyleAttr);
    }

    private void initAttributeSet(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ImageLoaderView, defStyle, 0);

        hasLoadingError = false;
        idLoadingImage = a.getResourceId(R.styleable.ImageLoaderView_loadingImage, 0);
        rounded = a.getBoolean(R.styleable.ImageLoaderView_rounded, false);
        url = a.getString(R.styleable.ImageLoaderView_url);
        VolleyRequest.initialize(getContext());

        a.recycle();
    }

    public void setUrl(String url) {
        this.url = url;
        invalidate();
    }

    @Override // No need for padding
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap imageAsset = getImageAsset();
        if (imageAsset != null) {
            canvas.drawBitmap(imageAsset, 0, 0, null);
        }
    }

    protected Bitmap getImageAsset() {
        Bitmap returnBitmap = null;

        if (hasLoadingError) {
            returnBitmap = BitmapFactory.decodeResource(getResources(), ERR_ASSET);
        } else if (imageBitmap != null) {
            returnBitmap = imageBitmap;
        } else if (url == null || url.trim().isEmpty()) {
            returnBitmap = BitmapFactory.decodeResource(getResources(), NO_ASSET);
        } else if (!isAnimationRunning()) {
            onLoadingStart();
            VolleyRequest.getInstance().getImageLoader().get(url, getImageListener());
        }

        returnBitmap = UtilsImagem.customFitBitmap(returnBitmap, getMeasuredWidth(), getMeasuredHeight());

        if (rounded) {
            returnBitmap = UtilsImagem.roundedBitmap(returnBitmap);
        }

        return returnBitmap;
    }

    protected void onLoadingStart() {
        if (idLoadingImage != 0) {
            setBackgroundResource(idLoadingImage);
        } else {
            startAnimation();
        }
    }

    protected void onLoadingEnd() {
        if (idLoadingImage != 0) {
            setBackgroundResource(0);
        } else {
            stopAnimation();
        }
    }

    protected void startAnimation() {
        if (isAnimationRunning()) {
            return;
        }

        setBackgroundResource(R.drawable.loading_dots_animation);
        frameAnimation = (AnimationDrawable) getBackground();
        frameAnimation.setVisible(true, true);
        frameAnimation.start();
    }

    protected void stopAnimation() {
        if (!isAnimationRunning()) {
            return;
        }

        frameAnimation.stop();
        setBackgroundResource(0);
        frameAnimation = null;
    }

    protected boolean isAnimationRunning() {
        return frameAnimation != null && frameAnimation.isRunning();
    }

    protected ImageLoader.ImageListener getImageListener() {
        return new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                if (bitmap != null) {
                    imageBitmap = bitmap;
                    hasLoadingError = false;
                    onLoadingEnd();
                    invalidate();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                hasLoadingError = true;
                ImageLoaderView.this.setOnClickListener(ImageLoaderView.this);
                onLoadingEnd();
                invalidate();
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (hasLoadingError) {
            hasLoadingError = false;
            invalidate();
        }
        if (otherOnClickListener != null) {
            otherOnClickListener.onClick(v);
        } else {
            super.setOnClickListener(null);
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(this);
        if (this != otherOnClickListener) {
            otherOnClickListener = l;
        }
    }

}
