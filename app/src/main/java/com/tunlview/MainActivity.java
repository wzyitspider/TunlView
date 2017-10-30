package com.tunlview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tunlview.view.TunlView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tunlview.view.TunlView.ZOOMLEVEL_INIT_1;
import static com.tunlview.view.TunlView.ZOOMLEVEL_INIT_2;
import static com.tunlview.view.TunlView.ZOOMLEVEL_INIT_3;
import static com.tunlview.view.TunlView.ZOOMLEVEL_INIT_4;
import static com.tunlview.view.TunlView.ZOOMLEVEL_INIT_5;
import static com.tunlview.view.TunlView.getTime;

public class MainActivity extends AppCompatActivity {

    TextView textView ;
    TunlView tunlView ;
    public static final int REFRESH_TIMERTV = 1;

    Button tv1 ;
    Button tv2 ;
    Button tv3 ;
    Button tv4 ;
    Button tv5 ;

    Button mode1;
    Button mode2;
    Button mode3;
    Button mode4;
    Handler handler =  new Handler(){

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what){
                case REFRESH_TIMERTV:
                    Message msg = Message.obtain();
                    msg.what = REFRESH_TIMERTV ;
                    handler.sendMessageDelayed(msg,1000);
                    getNowTime();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv_time);
        tunlView = (TunlView) findViewById(R.id.tunlview);
        textView.setText(""+tunlView.getValue());
        tv1 = (Button) findViewById(R.id.tv_1);
        tv2 = (Button) findViewById(R.id.tv_2);
        tv3 = (Button) findViewById(R.id.tv_3);
        tv4 = (Button) findViewById(R.id.tv_4);
        tv5 = (Button) findViewById(R.id.tv_5);
        mode1 = (Button) findViewById(R.id.mode1);
        mode2 = (Button) findViewById(R.id.mode2);
        mode3 = (Button) findViewById(R.id.mode3);
        mode4 = (Button) findViewById(R.id.mode4);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tunlView.setValueToSencond(ZOOMLEVEL_INIT_1);
                tunlView.postInvalidate();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tunlView.setValueToSencond(ZOOMLEVEL_INIT_2);
                tunlView.postInvalidate();

            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tunlView.setValueToSencond(ZOOMLEVEL_INIT_3);
                tunlView.postInvalidate();
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tunlView.setValueToSencond(ZOOMLEVEL_INIT_4);
                tunlView.postInvalidate();
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tunlView.setValueToSencond(ZOOMLEVEL_INIT_5);
                tunlView.postInvalidate();
            }
        });

        mode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tunlView.setMode(TunlView.Mode_1);
            }
        });
        mode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tunlView.setMode(TunlView.Mode_2);
            }
        });
        mode3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tunlView.setMode(TunlView.Mode_3);
            }
        });
        mode4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tunlView.setMode(TunlView.Mode_4);
            }
        });
        tunlView.setmListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                AXLog.e("wzytest","滑动 value:"+value);
                textView.setText(getTime(value));
            }
        });

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateformat.format(System.currentTimeMillis());
        textView.setText(dateStr);
        Message msg = Message.obtain();
        msg.what = REFRESH_TIMERTV ;
        handler.sendMessageDelayed(msg,1000);

    }


//    public void initData(){
//        OneDayRecordInfo info1 = new OneDayRecordInfo();
//        info1.setStartTime("00:00:00");
//        info1.setEndTime("23:59:59");
//
//
//        OneDayRecordInfo info2 = new OneDayRecordInfo();
//        info1.setStartTime("00:00:00");
//        info1.setEndTime("11:45:21");
//
//    }


    public void getNowTime(){
        String str = (String) textView.getText();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf1.parse(str);
            date.setTime(date.getTime()+1000);
            textView.setText(sdf1.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


//    public Date getdate(int i) // //获取前后日期 i为正数 向后推迟i天，负数时向前提前i天
//    {
////        Date dat = null;
////        Calendar cd = Calendar.getInstance();
////        cd.add(Calendar.DATE, i);
////        dat = cd.getTime();
////        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        Timestamp date = Timestamp.valueOf(dformat.format(dat));
////        return date;
//        Calendar calendar1 = Calendar.getInstance();
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
//        calendar1.add(Calendar.DATE, -3);
//        String three_days_ago = sdf1.format(calendar1.getTime());
//        System.out.println(three_days_ago);
//    }
}
