package com.tunlview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tunlview.OnValueChangeListener;
import com.tunlview.R;
import com.tunlview.util.AXLog;
import com.tunlview.view.TunlView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tunlview.view.TunlView.getTime;

public class MainActivity extends AppCompatActivity {

    TextView textView ;
    TunlView tunlView ;
    public static final int REFRESH_TIMERTV = 1;

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

}
