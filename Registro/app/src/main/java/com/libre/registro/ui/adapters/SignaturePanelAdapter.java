package com.libre.registro.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.libre.registro.ui.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class SignaturePanelAdapter extends View {

    private static final float TOUCH_TOLERANCE = 4;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private TextView tvFirmaAqui;

    private final Paint mPaint;
    Path path = new Path();
    float mX, mY;
    private int width, height;
    private Context context;

    public SignaturePanelAdapter(Context context) {
        super(context);
        this.context=context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(2f);
        mPaint.setARGB(0xff, 0x33, 0x33, 0x33);
    }

    public SignaturePanelAdapter(Context context,TextView tvFirmaAqui) {
        super(context);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(2f);
        mPaint.setARGB(0xff, 0x33, 0x33, 0x33);
        this.tvFirmaAqui = tvFirmaAqui;
    }


    public void clear() {
        if (this.mCanvas != null) {
            this.mCanvas.drawColor(Color.WHITE);
            invalidate();

            tvFirmaAqui.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int curW = this.mBitmap != null ? this.mBitmap.getWidth() : 0;
        int curH = this.mBitmap != null ? this.mBitmap.getHeight() : 0;
        if (curW >= w && curH >= h) {
            return;
        }

        if (curW < w) curW = w;
        if (curH < h) curH = h;

        width = w;
        height = h;

        this.mBitmap = Bitmap.createBitmap(curW, curH, Bitmap.Config.RGB_565);

        this.mCanvas = new Canvas(this.mBitmap);
        this.mCanvas.drawColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.mBitmap != null) {
            canvas.drawBitmap(this.mBitmap, 0, 0, null);
        }
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                tvFirmaAqui.setVisibility(View.GONE);
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;

            default:
                break;
        }
        return true;
    }


    private void touchStart(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        mX = x;
        mY = y;

    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mCanvas.drawPath(path, mPaint);
            mX = x;
            mY = y;

        }
    }


    public int getW(){
        return width;
    }

    public int getH(){
        return height;
    }

    public  Bitmap getBitmap(){
        return this.mBitmap;
    }
    public  String saveBitmap(){
        File file=null;
        try {

            Bitmap bitmap = this.mBitmap;
            file = new File(Constants.PATH,Constants.FILE_NAME);
            FileOutputStream fOut = new FileOutputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            byte[] bitmapdata = bos.toByteArray();
            Log.i("##################", "" + bitmap.getByteCount());
            fOut.write(bitmapdata);
            fOut.flush();
            fOut.close();


        }catch(Exception ex){
            Log.e("###########",""+ex.getMessage());
        }
        return file.getAbsolutePath();
    }
}