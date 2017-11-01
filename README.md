##一、前言
最近公司需要，需要一个视频播放条控件，可以将控件与时间对应起来，也可以对控件进行移动和缩放。（PS：公司是做摄像头的）移动到某个位置就播放该位置对应时间戳的视频录像，当然，并非任何时间点都有录像文件，所有录像的时间段用蓝色覆盖，代表该段时间可以进行录像回放，不在该蓝色区域内代表不能回放。最终完成效果如下

### 整体效果截图（小公司,美工没有!UI设计,奇丑!凑合着看吧）
![这里写图片描述]![这里写图片描述](http://img.blog.csdn.net/20171101162040025?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd3p5OTAxMjEz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

### 缩放效果截图
![缩放效果](http://img.blog.csdn.net/20171101145325971?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd3p5OTAxMjEz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

### 滑动效果截图
![滑动效果](http://img.blog.csdn.net/20171101144819352?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd3p5OTAxMjEz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)



##二、代码实现
我们需要一个自定义View，重写其onLayout方法，获取其宽高	

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
    }

然后重写onDraw方法：分别绘制刻度，中间的红线（代表当前刻度尺滑动到的时间点），还有蓝色块（代表有录像的区域）。这里的思路是，我们把整个控件分为左右两块区域，由中间往两边绘制刻度。其主要代码：
	
	  for (int i = 0; drawCount < width * 2; i++) {
            xPosition = (mWidth / 2 + mMove) + i * mDensity * (mLineDivider);
            if (((mValue + i)) % largeScaleValue == 0) {
                canvas.drawLine(xPosition, 0, xPosition, mDensity * ITEM_MAX_HEIGHT, linePaint);
                if ((((mValue + i)) / largeScaleValue % useString.length) < 0) {
                    canvas.drawText(useString[useString.length + (((mValue + i)) / largeScaleValue % useString.length)], countLeftStart(mValue + i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                } else {
                    canvas.drawText(useString[((mValue + i)) / largeScaleValue % useString.length], countLeftStart(mValue + i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                }

            } else {
                if ((mValue + i) % smallScaleValue == 0) {
                    canvas.drawLine(xPosition, 0, xPosition, mDensity * ITEM_MIN_HEIGHT, linePaint);
                }
            }

            xPosition = (mWidth / 2 + mMove) - i * mDensity * (mLineDivider);
            if (( (mValue - i)) % largeScaleValue == 0) {
                canvas.drawLine(xPosition, 0, xPosition, mDensity * ITEM_MAX_HEIGHT, linePaint);
                if ((( (mValue - i)) / largeScaleValue % useString.length) < 0) {
                    canvas.drawText(useString[useString.length + (((mValue - i)) / largeScaleValue % useString.length)], countLeftStart(mValue - i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                } else {
                    canvas.drawText(useString[( (mValue - i)) / largeScaleValue % useString.length], countLeftStart(mValue - i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                }
            } else {
                if ( (mValue - i) % smallScaleValue == 0) {
                    canvas.drawLine(xPosition, 0, xPosition, mDensity * ITEM_MIN_HEIGHT, linePaint);
                }
            }
            drawCount += mDensity * (mLineDivider);

        }
绘制中间的红线代码就很简单了，找到视图的中间，直接绘制
	
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
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight, redPaint);
    }
绘制代表有录像视频的蓝色区域

	    public void drawShadow(Canvas canvas){
        float startXPosition ;
        float endXPosition ;
        // TODO: 2017/10/24  画阴影面积
        for (int j = 0; j < list.size(); j++) {
            RecordInfo info = list.get(j);
            int startvalue = info.getStartValue();
            int endvalue = info.getEndValue();

            //获取当前得value
            AXLog.e("wzytest","getNowValue:"+getNowValue() +"mvalue:"+mValue);

            startXPosition = mWidth/2 - (mValue-startvalue) * mDensity * (mLineDivider);
            endXPosition = mWidth/2 + (endvalue-mValue) * mDensity * (mLineDivider);

            canvas.drawRect(startXPosition,0,endXPosition,mHeight,shadowPaint);
        }
    }

我们也要搞清楚时间与刻度之间的转换

	/**
     * 获取时间戳所对应的mvalue
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
     * 获取mValue 所对应时间
     * @param mValue
     * @return
     */
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
当手指滑动和缩放所进行的处理
		
	case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    mMove = xPosition - mlastX;
                    xMove = (int) (mMove / (mLineDivider * mDensity));
                    mValue -= xMove;
                    notifyValueChange();
                    invalidate();
                }
                break;

还有最难处理的问题在于，当进行缩放时候，如果刻度尺变得过大或者过小的时候我们应该让刻度尺的精度模式也进行变化，适应页面的变化，不然太拥挤导致字体重叠，太松动导致整个页面可能一个刻度都没有。这个处理比较复杂，整体思路就是刻度尺的间隔会随着手指的缩放进行相应的缩放，当刻度尺大到一定程序或者小到一定程度就对刻度尺的精度进行改变

	 else if (event.getPointerCount() == 2) {
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
                        if(2*mLineDivider<LINE_DIVIDER){
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
                        if(3*mLineDivider<LINE_DIVIDER){
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
                        if(4*mLineDivider<LINE_DIVIDER){
                            // 复位，转变刻度尺模式
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString3;
                            Mode = Mode_3;
                            valueToSencond = 4*valueToSencond;
                            AXLog.e("wzytest","mvalue:"+mValue);
                            //重新获取当前value
                            mValue = getNowValue();
                            //重新计算蓝色录像时间轴
                            initData();
                        }

                    }else if(currentStatus==STATUS_ZOOM_IN&&Mode==Mode_3){
                        AXLog.e("wzytest","跳转到了最后得刻度尺模式");
                        if(2*mLineDivider<LINE_DIVIDER){
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
                        if(mLineDivider/2>LINE_DIVIDER){
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
                        if(mLineDivider/4>LINE_DIVIDER){
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString2;
                            Mode = Mode_2;
                            valueToSencond = valueToSencond/4;
                            AXLog.e("wzytest","mvalue:"+mValue);
                            //重新获取当前value
                            mValue = getNowValue();
                            AXLog.e("wzytest","重新获取的value:"+mValue);
                            //重新计算蓝色录像时间轴
                            initData();
                        }
                    }else if(currentStatus==STATUS_ZOOM_OUT&&Mode==Mode_2){
                        if(mLineDivider/3>LINE_DIVIDER){
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
                        if(mLineDivider/2>LINE_DIVIDER){
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

该部分代码比较复杂，建议有兴趣的读者下载源码进行研究。

##三、其它相关
作者开启了一个异步线程定时一秒让录像文件往后增加一秒，这么做的意图只是模拟正在录像的状态，让蓝色的区域跟着时间走动

##四、总结
遇到比较复杂的问题先抽丝剥茧的找出问题的本质，把问题慢慢细化，一步步解决，千里之行始于足下，不积跬步何以至千里，不积小流何以成江河

###[源码下载请点这里](https://gitee.com/wzy901213145499/Tunlview.git)


