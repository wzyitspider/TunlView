package com.tunlview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.tunlview.AXLog;
import com.tunlview.OnValueChangeListener;
import com.tunlview.OneDayRecordInfo;
import com.tunlview.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */

public class TunlView extends View {
    /**
     * 初始化状态常量
     */
    public static final int STATUS_INIT = 1;

    /**
     * 图片放大状态常量
     */
    public static final int STATUS_ZOOM_OUT = 2;

    /**
     * 图片缩小状态常量
     */
    public static final int STATUS_ZOOM_IN = 3;

    /**
     * 记录两指同时放在屏幕上时，中心点的横坐标值
     */
    private float centerPointX;

    /**
     * 记录两指同时放在屏幕上时，中心点的纵坐标值
     */
    private float centerPointY;

    /**
     * 记录按下两指之间的距离
     */
    private double lastFingerDis;

    /**
     * 记录当前操作的状态，可选值为STATUS_INIT、STATUS_ZOOM_OUT、STATUS_ZOOM_IN和STATUS_MOVE
     */
    private int currentStatus;


    private static final float TEXT_SIZE = 12;

    public int mWidth;
    public int mHeight;
    int mlastX, mMove;


    public static final int ZOOMLEVEL_INIT_1 = 10;

    public static int valueToSencond = 100 ;  //每个value代表多少秒


    private int REFRESH_TIME =  valueToSencond*1000;

    int mValue;

    int modType = 360 ,mModType = 360; // 30*60/5 一格大刻度量级(1个小时)


    float mLineDivider = 3;//记录一刻度屏幕上得距离
    private static  float lastItemDivider = 3;//缩放后一刻度得屏幕距离

    private float mDensity;
    private float ITEM_MAX_HEIGHT = 30;
    private float ITEM_MIN_HEIGHT = 15;
    private float textWidth;

    Calendar calendar1 = Calendar.getInstance();
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    List<OneDayRecordInfo> list = new ArrayList<>();
    private String[] timeString0 = {"00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30",
            "05:00", "05:30", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
            "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30",
            "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30",
            "23:00", "23:30"};

    private String[] timeString1 = {"00:00",  "01:00",  "02:00", "03:00",  "04:00",
            "05:00",  "06:00",  "07:00",  "08:00", "09:00",  "10:00",
            "11:00",  "12:00",  "13:00",  "14:00",  "15:00",  "16:00",
            "17:00",  "18:00",  "19:00",  "20:00",  "21:00", "22:00",
            "23:00"};

    private String[] timeString2 = {"00:00", "03:00", "06:00", "9:00", "12:00", "15:00","18:00","21:00"};


//    private String[] timeString3 = {"00:00", "6:00","12:00","18:00"};


    private String[] timeString3 = {"00:00", "12:00"};

    private String[] timeString4 = {"00:00"};

    public int Mode = Mode_1 ;  // 当前的刻度尺缩放模式，默认模式一

    public static final int Mode_0 = 0; //模式一,当前刻度尺显示timeString0
    public static final int Mode_1 = 1; //模式一,当前刻度尺显示timeString1
    public static final int Mode_2 = 2; //模式一,当前刻度尺显示timeString2
    public static final int Mode_3 = 3; //模式一,当前刻度尺显示timeString3
    public static final int Mode_4 = 4; //模式一,当前刻度尺显示timeString4
    public static final int Mode_5 = 5; //模式一,当前刻度尺显示timeString5

    public String[] useString;
    private float scaledRatio;

    public OnValueChangeListener getmListener() {
        return mListener;
    }

    public void setmListener(OnValueChangeListener mListener) {
        this.mListener = mListener;
    }

    private OnValueChangeListener mListener;
    Paint linePaint = new Paint();
    Paint shadowPaint = new Paint();


    TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);


    public TunlView(Context context) {
        super(context);
    }

    public TunlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        useString = timeString1;
        mDensity = getContext().getResources().getDisplayMetrics().density;
        setBackgroundResource(R.drawable.bg_wheel);

        //过二十五秒让mValue自增一次
        postDelayed(new Runnable() {
            @Override
            public void run() {
                postDelayed(this, valueToSencond*1000);
                mValue++;
                postInvalidate();
            }
        },  valueToSencond*1000);


        initData();
        shadowPaint.setStrokeWidth(6);
        shadowPaint.setColor(Color.parseColor("#83D7DD"));
        shadowPaint.setAlpha(101);

    }


    public void initData() {
        list.clear();
        mValue = getNowValue();

        Date d = new Date();
        OneDayRecordInfo info1 = new OneDayRecordInfo();


        OneDayRecordInfo info2 = new OneDayRecordInfo();
        info2.setStartTime(d.getTime() - 60 * 60 * 1000 * 8);
        info2.setEndTime(d.getTime() - 60 * 60 * 1000 * 7);
        info2.setStartValue(getmValue(info2.getStartTime()));
        info2.setEndValue(getmValue(info2.getEndTime()));

        info1.setStartTime(d.getTime() - 60 * 60 * 1000);
        info1.setEndTime(d.getTime());
        // info1.setCurrentDate("2017-10-23");
        info1.setStartValue(getmValue(info1.getStartTime()));
        info1.setEndValue(getmValue(info1.getEndTime()));

        list.add(info2);
        list.add(info1);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                postDelayed(this, 60 * 1000);
                OneDayRecordInfo info = new OneDayRecordInfo();
                info.setStartTime(System.currentTimeMillis() - 60 * 1000);
                info.setEndTime(System.currentTimeMillis());
                info.setStartValue(getmValue(info.getStartTime()));
                info.setEndValue(getmValue(info.getEndTime()));
                AXLog.e("wzytest", "info.StartTime:" + info.getStartTime() + " info.getEndTime():" + info.getEndTime());
                addNoRepeatList(info);
                postInvalidate();
            }

        }, 60 * 1000);

    }

    private void addNoRepeatList(OneDayRecordInfo info) {
        OneDayRecordInfo lastInfo = list.get(list.size() - 1);

        if (info.getStartTime() - lastInfo.getEndTime() < 1000) {
            AXLog.e("wzytest", "前一个数据的最后时间小于当前的开始时间不超过一秒");
            lastInfo.setEndTime(info.getEndTime());
            lastInfo.setEndValue(info.getEndValue());
        } else {
            list.add(info);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawScaleLine(canvas);
        drawMiddleLine(canvas);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    //划线
    public void drawScaleLine(Canvas canvas) {
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.BLACK);

        textPaint.setTextSize(TEXT_SIZE * mDensity);
        textPaint.setTextSize(16);
        textWidth = Layout.getDesiredWidth("0", textPaint);

        int width = mWidth, drawCount = 0;

        float xPosition;

        for (int i = 0; drawCount < width * 2; i++) {
            xPosition = (mWidth / 2 + mMove) + i * mDensity * (mLineDivider);
            if ((ZOOMLEVEL_INIT_1 * (mValue + i)) % mModType == 0) {
                canvas.drawLine(xPosition, 0, xPosition, mDensity * ITEM_MAX_HEIGHT, linePaint);
                if (((ZOOMLEVEL_INIT_1 * (mValue + i)) / mModType % useString.length) < 0) {
                    canvas.drawText(useString[useString.length + ((ZOOMLEVEL_INIT_1 * (mValue + i)) / mModType % useString.length)], countLeftStart(mValue + i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                } else {
                    canvas.drawText(useString[(ZOOMLEVEL_INIT_1 * (mValue + i)) / mModType % useString.length], countLeftStart(mValue + i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                }
            } else {
                if (ZOOMLEVEL_INIT_1 * (mValue + i) % 60 == 0) {
                    canvas.drawLine(xPosition, 0, xPosition, mDensity * ITEM_MIN_HEIGHT, linePaint);
                }
            }
            // TODO: 2017/10/24  画阴影面积
            for (int j = 0; j < list.size(); j++) {
                OneDayRecordInfo info = list.get(j);
                int startvalue = info.getStartValue();
                int endvalue = info.getEndValue();
                if (mValue + i <= endvalue && mValue + i >= startvalue) {
                    canvas.drawLine(xPosition, 0, xPosition, mHeight, shadowPaint);
                }
            }

            xPosition = (mWidth / 2 + mMove) - i * mDensity * (mLineDivider);
            if ((ZOOMLEVEL_INIT_1 * (mValue - i)) % mModType == 0) {
                canvas.drawLine(xPosition, 0, xPosition, mDensity * ITEM_MAX_HEIGHT, linePaint);
                if (((ZOOMLEVEL_INIT_1 * (mValue - i)) / mModType % useString.length) < 0) {
                    canvas.drawText(useString[useString.length + ((ZOOMLEVEL_INIT_1 * (mValue - i)) / mModType % useString.length)], countLeftStart(mValue - i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                } else {
                    canvas.drawText(useString[(ZOOMLEVEL_INIT_1 * (mValue - i)) / mModType % useString.length], countLeftStart(mValue - i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                }
            } else {
                if (ZOOMLEVEL_INIT_1 * (mValue - i) % 60 == 0) {
                    canvas.drawLine(xPosition, 0, xPosition, mDensity * ITEM_MIN_HEIGHT, linePaint);
                }
            }

            // TODO: 2017/10/24  画阴影面积
            //canvas.drawLine(xPosition, 0, xPosition + ITEM_ONE_DIVIDER, mHeight, shadowPaint);
            for (int j = 0; j < list.size(); j++) {
                OneDayRecordInfo info = list.get(j);
                int startvalue = info.getStartValue();
                int endvalue = info.getEndValue();
                if (mValue - i <= endvalue && mValue - i >= startvalue) {
                    canvas.drawLine(xPosition, 0, xPosition, mHeight, shadowPaint);
                }
            }
            drawCount += mDensity * (mLineDivider);
        }


    }


    /**
     * 计算数字显示位置的辅助方法
     *
     * @param value
     * @param xPosition
     * @param textWidth
     * @return
     */
    private float countLeftStart(int value, float xPosition, float textWidth) {
        float xp;
        xp = xPosition - (textWidth * 5 / 2); //从2.5个字开始写
        return xp;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int xPosition = (int) event.getX();
        int xMove;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    // 当有两个手指按在屏幕上时，计算两指之间的距离
                    lastFingerDis = distanceBetweenFingers(event);
                    AXLog.e("wzytest", "当有两个手指按在屏幕上时，计算两指之间的距离" + lastFingerDis);
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mlastX = xPosition;
                mMove = 0;
                Log.d("wzytest", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                //当滑动到停止，改变当前刻度值
                xMove = (int) (mMove / (mLineDivider * mDensity));
                mValue -= xMove;
                notifyValueChange();
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    mMove = xPosition - mlastX;
                    xMove = (int) (mMove / (mLineDivider * mDensity));
                    mValue -= xMove;
                    notifyValueChange();
                    invalidate();
                } else if (event.getPointerCount() == 2) {
                    // 有两个手指按在屏幕上移动时，为缩放状态
                    centerPointBetweenFingers(event);
                    double fingerDis = distanceBetweenFingers(event);

                    if (fingerDis > lastFingerDis) {
                        currentStatus = STATUS_ZOOM_OUT;
                    } else {
                        currentStatus = STATUS_ZOOM_IN;
                    }

                    if(Mode==Mode_0&&currentStatus==STATUS_ZOOM_OUT||Mode==Mode_4&&currentStatus==STATUS_ZOOM_IN){
                        AXLog.e("wzytest","Mode_0 时放大 和 Mode_4 时候缩小不做任何处理");
                    }else{
                        scaledRatio = (float) (fingerDis / lastFingerDis  );
                        shadowPaint.setStrokeWidth(6*scaledRatio);
                        mLineDivider =  lastItemDivider * scaledRatio; //缩放后一刻度在屏幕上的距离
                    }

                    if(currentStatus==STATUS_ZOOM_IN&&Mode==Mode_0){
                        if(2*mLineDivider<2){
                            // 复位，转变刻度尺模式
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString1;
                            Mode = Mode_1;
                            valueToSencond = 2*valueToSencond;
                            AXLog.e("wzytest","mvalue:"+mValue);
                            //重新获取当前value
                            mValue = getNowValue();
                            //重新计算蓝色录像时间轴
                            initData();
                        }
                    }else if(currentStatus==STATUS_ZOOM_IN&&Mode==Mode_1){
                        if(3*mLineDivider<2){
                            // 复位，转变刻度尺模式
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString2;
                            Mode = Mode_2;
                            valueToSencond = 3*valueToSencond;
                            AXLog.e("wzytest","mvalue:"+mValue);
                            //重新获取当前value
                            mValue = getNowValue();
                            //重新计算蓝色录像时间轴
                            initData();
                        }
                    }else if(currentStatus==STATUS_ZOOM_IN&&Mode==Mode_2){
                        if(3*mLineDivider<2){
                            // 复位，转变刻度尺模式
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString3;
                            Mode = Mode_3;
                            valueToSencond = 3*valueToSencond;
                            AXLog.e("wzytest","mvalue:"+mValue);
                            //重新获取当前value
                            mValue = getNowValue();
                            //重新计算蓝色录像时间轴
                            initData();
                        }

                    }else if(currentStatus==STATUS_ZOOM_IN&&Mode==Mode_3){
                        AXLog.e("wzytest","跳转到了最后得刻度尺模式");
                        if(2*mLineDivider<2){
                            // 复位，转变刻度尺模式
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString4;
                            Mode = Mode_4;
                            valueToSencond = 2*valueToSencond;
                            // 重新获取当前value
                            mValue = getNowValue();
                            //重新计算蓝色录像时间轴
                            initData();
                        }
                    }


                    if(currentStatus==STATUS_ZOOM_OUT&&Mode==Mode_4){
                        if(mLineDivider/2>2){
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString3;
                            Mode = Mode_3;
                            valueToSencond = valueToSencond/2;
                            //重新获取当前value
                            mValue = getNowValue();
                            //重新计算蓝色录像时间轴
                            initData();
                        }
                    }else if(currentStatus==STATUS_ZOOM_OUT&&Mode==Mode_3){
                        if(mLineDivider/3>2){
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString2;
                            Mode = Mode_2;
                            valueToSencond = valueToSencond/3;
                            AXLog.e("wzytest","mvalue:"+mValue);
                            //重新获取当前value
                            mValue = getNowValue();
                            AXLog.e("wzytest","重新获取的value:"+mValue);
                            //重新计算蓝色录像时间轴
                            initData();
                        }
                    }else if(currentStatus==STATUS_ZOOM_OUT&&Mode==Mode_2){
                        if(mLineDivider/3>2){
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString1;
                            Mode = Mode_1;
                            valueToSencond = valueToSencond/3;
                            mValue = getNowValue();
                            //重新计算蓝色录像时间轴
                            initData();
                        }
                    }else if(currentStatus==STATUS_ZOOM_OUT&&Mode==Mode_1){
                        if(mLineDivider/2>2){
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString0;
                            Mode = Mode_0;
                            valueToSencond = valueToSencond/2;
                            //重新获取当前value
                            mValue = getNowValue();
                            //重新计算蓝色录像时间轴
                            initData();
                        }
                    }

                    AXLog.e("wzytest","itemDivider:"+mLineDivider+" lastItemDivider:"+lastItemDivider);
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 2) {
                    //lastValueTosecond = valueToSencond;
                    lastItemDivider = mLineDivider;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                AXLog.e("wzytest", "ACTION_CANCEL.......");
                break;
            default:
                break;
        }
        return true;
//        return super.onTouchEvent(event);
    }

    private void notifyValueChange() {

        if (null != mListener) {
            mListener.onValueChange(mValue);
        }
    }


    /**
     * 获取当前刻度值
     *
     * @return
     */
    public float getValue() {
        return mValue;
    }

    /**
     * 设置当前刻度值
     *
     * @param mValue
     */
    public void setmValue(int mValue) {
        this.mValue = mValue;
    }

    /**
     * 画中间的红色指示线
     *
     * @param canvas
     */
    private void drawMiddleLine(Canvas canvas) {
        int indexWidth = 5;
        String color = "#66999999";
        Paint redPaint = new Paint();
        redPaint.setStrokeWidth(indexWidth);
        redPaint.setColor(Color.RED);
        // canvas.drawLine(mWidth / 2, 0, mWidth / 2 , mHeight, shadowPaint);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight, redPaint);

    }

    public static int getNowValue() {
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        Log.e("wzytest", "hour:" + hour + " minute:" + minute + " second:" + second);

        //一个mvalue代表25 秒，两个刻度中间间隔了（60/5）个mvalue
        return ((hour * 60 + minute) * 60 + second) / valueToSencond ;
        // return 360;
    }

    public static String getTime(float mValue) {
        // TODO: 2017/10/23  超过24小时 和 少于0小时的处理

        int day = (int) (mValue *  valueToSencond / (3600 * 24));  // 天数
        int hour = (int) ((mValue *  valueToSencond - (60 * 60 * 24) * day) / 3600);
        int minute = (int) (mValue * valueToSencond - 3600 * hour - (60 * 60 * 24) * day) / 60;
        int second = (int) mValue * valueToSencond - hour * 3600 - minute * 60 - (60 * 60 * 24) * day;

        AXLog.e("wzytest", "hour:" + hour + " minute:" + minute + " second:" + second + " day:" + day);
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        /**
         *  从00:00:00开始设置时间
         */
        calendar1.add(Calendar.DATE, day);
        calendar1.set(Calendar.HOUR_OF_DAY, hour);
        calendar1.set(Calendar.MINUTE, minute);
        calendar1.set(Calendar.SECOND, second);

        String moveDate = sdf1.format(calendar1.getTime());
        return moveDate;
    }


    /**
     * 获取录像起始或者结束时间的mvalue值
     *
     * @param time 录像时间段time
     * @return
     */
    private int getmValue(long time) {
        /**
         *  从00:00:00开始设置时间
         */
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        long l1 = calendar1.getTimeInMillis();
        return (int) (time - l1) / (1000 * valueToSencond);
    }

    /**
     * 计算两个手指之间的距离。
     *
     * @param event
     * @return 两个手指之间的距离
     */
    private double distanceBetweenFingers(MotionEvent event) {
        float disX = Math.abs(event.getX(0) - event.getX(1));
        float disY = Math.abs(event.getY(0) - event.getY(1));
        return Math.sqrt(disX * disX + disY * disY);
    }

    /**
     * 计算两个手指之间中心点的坐标。
     *
     * @param event
     */
    private void centerPointBetweenFingers(MotionEvent event) {
        float xPoint0 = event.getX(0);
        float yPoint0 = event.getY(0);
        float xPoint1 = event.getX(1);
        float yPoint1 = event.getY(1);
        centerPointX = (xPoint0 + xPoint1) / 2;
        centerPointY = (yPoint0 + yPoint1) / 2;
    }


}
