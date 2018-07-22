package com.example.rahul.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.MotionEvent;


public class AnimView extends AppCompatImageView  {
    private Context mContext;
    int x = -1;
    int y = -1;
    private int xVelocity = 10;
    private int yVelocity = 5;
    private Handler h;
    private final int FRAME_RATE = 25;

    boolean cut = false;
    float touchX=0, touchy=0;
    float endX, endY;
    float startX, startY;
    Paint pain = new Paint();
    Paint paint = new Paint();
    Paint bg = new Paint();
    Paint bg2 = new Paint();
    Paint t1 = new Paint();
    Paint t2 = new Paint();

    Canvas c;
    boolean horizontal = true;
    boolean gameOver = false;


    public AnimView(Context context, AttributeSet attrs)  {
        super(context, attrs);
        mContext = context;
        h = new Handler();
        startX = 0;
        startY = 0;
        endX = 1080;
        endY = 1080;
        touchX = endX;
        touchy = endY;
        paint.setColor(Color.BLUE);
        paint.setTextSize(100);
        paint.setTextAlign(Paint.Align.CENTER);
        pain.setColor(Color.WHITE);
        bg.setColor(Color.WHITE);
        bg2.setColor(Color.BLACK);
        t1.setColor(Color.BLACK);
        t2.setColor(Color.BLACK);
        t1.setTextSize(100);
        t1.setTextAlign(Paint.Align.CENTER);
        t2.setTextSize(100);
        t2.setTextAlign(Paint.Align.CENTER);

    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };


    protected void onDraw(Canvas c) {
        if(!gameOver){
            this.c = c;
            c.drawLine(startX, startY,startX+1,endY,pain);
            c.drawLine(startX, startY,endX,startY+1,pain);
            c.drawLine(endX-1, startY,endX,endY,pain);
            c.drawLine(startX, endY,endX,endY+1,pain);
            draw();
        }
        else {
            c.drawText("Restart",(c.getWidth()/2),1380,t1);
            c.drawText("Game over",(c.getWidth()/2),(c.getHeight()/2),t1);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        Log.i("OnTouch",event.getX()+"'"+event.getY());

        if((1180<event.getY())&&(event.getY()<1280)){
            horizontal = !horizontal;
        }
        else if((1280<event.getY())&&(event.getY()<1380)){

        }
        else {
            if(horizontal)
                touchX = event.getX();
            else
                touchy = event.getY();
            cut = true;
            this.draw();
        }

        return super.onTouchEvent(event);
    }

    public void draw(){
        Bitmap ball = drawableToBitmap(mContext.getResources().getDrawable(R.drawable.ball));
        if(cut){
            if(horizontal){
                if((touchX-10>=x)&&(touchX<=x+ball.getWidth()-10)){
                    gameOver = true;
                    Log.i("Values", x+" "+touchX+" "+ball.getWidth());
                }
                else {
                    if(x>touchX)
                    {
                        startX = touchX;
                        Log.i("Values", x+" "+touchX+" "+endX);
                    }
                    else
                        endX = touchX;
                    cut = false;
                }
            } else {
                if((touchy-5>=y)&&(touchy<=y+ball.getHeight()-5)){
                    gameOver = true;
                    Log.i("Values", x+" "+touchX+" "+ball.getWidth());
                }
                else {
                    if(y>touchy)
                    {
                        startY = touchy;
                        Log.i("Values", x+" "+touchX+" "+endX);
                    }
                    else
                        endY = touchy;
                    cut = false;
                }
            }
        }

        if (x<0 && y <0) {
            x = this.getWidth()/2;
            y = this.getHeight()/2;
        } else {
            x += xVelocity;
            y += yVelocity;
            if ((x > endX - ball.getWidth()) || (x < startX)) {
                xVelocity = xVelocity*-1;
            }
            if ((y > endY - ball.getHeight()) || (y < startY)) {
                yVelocity = yVelocity*-1;
            }
        }

        c.drawRect(0,1182,1180,1378,bg);
        c.drawRect(0,1278,1180,1282,bg2);
        c.drawText("Your score: "+((int)(((endX-startX)*(endY-startY))/10000)),(c.getWidth()/2),1180,paint);
        c.drawText("Flip orientation",(c.getWidth()/2),1280,t1);
        c.drawText("Restart",(c.getWidth()/2),1380,t1);
        c.drawBitmap(ball, x, y, null);
        h.postDelayed(r, FRAME_RATE);
    }

































//Dont touch at all

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}