package com.example.samprice.cmsc434doodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

import android.graphics.Bitmap;

/**
 * Created by samprice on 3/9/16.
 */
public class DoodleView extends View{

    private Paint _paintDoodle, _canvasPaint;
    private Path _path;
    private Canvas _drawCanvas;
    private Bitmap _canvasBitmap;
    private int _paintColor = 0xFF660000;
    private float _brushSize, _lastBrushSize;
    private boolean _erase = false;
    private int paintAlpha = 255;


    public DoodleView(Context context) {
        super(context);
        init(null, 0);
    }
    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DoodleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }
    private void init(AttributeSet attrs, int defStyle){
        _brushSize = getResources().getInteger(R.integer.medium_size);
        _lastBrushSize = _brushSize;

        _paintDoodle = new Paint();
        _path = new Path();
        _paintDoodle.setColor(_paintColor);
        _paintDoodle.setAntiAlias(true);
        _paintDoodle.setStrokeWidth(_brushSize);
        _paintDoodle.setStyle(Paint.Style.STROKE);
        _paintDoodle.setStrokeJoin(Paint.Join.ROUND);
        _paintDoodle.setStrokeCap(Paint.Cap.ROUND);

        _canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int x, int y, int oldx, int oldy) {
        super.onSizeChanged(x, y, oldx, oldy);
        _canvasBitmap = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        _drawCanvas = new Canvas(_canvasBitmap);

    }

    @Override
    public void onDraw(Canvas canvas){

        canvas.drawBitmap(_canvasBitmap, 0, 0, _canvasPaint);
        canvas.drawPath(_path, _paintDoodle);
    }
    @Override
    public boolean onTouchEvent (MotionEvent motionEvent) {
        float touchX = motionEvent.getX();
        float touchY = motionEvent.getY();

        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                _path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                _path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                _drawCanvas.drawPath(_path, _paintDoodle);
                _path.reset();
                break;
        }
        invalidate();
        return true;
    }

    public void setColor(String newColor){
        invalidate();
        _paintColor = Color.parseColor(newColor);
        _paintDoodle.setColor(_paintColor);
    }

    public void setBrushSize(float newSize){
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        _brushSize=pixels;
        _paintDoodle.setStrokeWidth(_brushSize);
    }

    public void setLastBrushSize(float lastSize){
        _lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return _lastBrushSize;
    }

    public void setErase(boolean isErase){
        _erase=isErase;
        if(_erase) _paintDoodle.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else _paintDoodle.setXfermode(null);
    }
    public void startNew(){
        _drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public int getPaintAlpha(){
        return Math.round((float)paintAlpha/255*100);
    }

    public void setPaintAlpha(int newAlpha){
        paintAlpha=Math.round((float)newAlpha/100*255);
        _paintDoodle.setColor(_paintColor);
        _paintDoodle.setAlpha(paintAlpha);
    }

}
