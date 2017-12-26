package com.whompum.pennydialog.dialog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by bryan on 12/25/2017.
 */

public class ShadowDrawable extends ShapeDrawable {


    @DrawableRes
    public static final int SHADOW_IMAGE = R.drawable.image_shadow;


    private Bitmap shadow;


    public ShadowDrawable(final Shape shape, final Resources resources){
        super(shape);


        if(Build.VERSION.SDK_INT >= 21)
            shadow = ((BitmapDrawable)resources.getDrawable(SHADOW_IMAGE, null)).getBitmap();
        else
            shadow = ((BitmapDrawable)resources.getDrawable(SHADOW_IMAGE)).getBitmap();


        setShaderFactory(this.shaderFactory);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void setAlpha(int alpha) {

    }


    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }


    public ShaderFactory shaderFactory = new ShaderFactory() {
        @Override
        public Shader resize(int width, int height) {
            final Bitmap scaledShadow = Bitmap.createScaledBitmap(shadow, width, height+5 , true);
            return new BitmapShader(scaledShadow, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }
    };

}
