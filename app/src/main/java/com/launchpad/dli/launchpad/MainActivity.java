package com.launchpad.dli.launchpad;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final static String DEBUG_TAG = "mainactivity";

    TextView textView;

    TextView originalXTextView;
    TextView originalYTextView;
    TextView currentXTextView;
    TextView currentYTextView;

    static int GLOBAL_TOUCH_POSITION_X = 0;
    static int GLOBAL_TOUCH_CURRENT_POSITION_X = 0;

    static int GLOBAL_TOUCH_POSITION_Y = 0;
    static int GLOBAL_TOUCH_CURRENT_POSITION_Y = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.statusText);

        originalXTextView = (TextView) findViewById(R.id.originalX);
        originalYTextView = (TextView) findViewById(R.id.originalY);
        currentXTextView = (TextView) findViewById(R.id.currentX);
        currentYTextView = (TextView) findViewById(R.id.currentY);
    }

    private boolean handleMultiTouch(MotionEvent event, int fingerCount){
        int count =event.getPointerCount();

        boolean flag = false;

        if(count== fingerCount){
            int action = MotionEventCompat.getActionMasked(event);
            int actionIndex = event.getActionIndex();
            String actionStr;

            switch (action){

                case MotionEvent.ACTION_DOWN:
                    GLOBAL_TOUCH_POSITION_X = (int)event.getX();
                    GLOBAL_TOUCH_POSITION_Y = (int)event.getX();
                case MotionEvent.ACTION_UP:
                    GLOBAL_TOUCH_CURRENT_POSITION_X = 0;
                    GLOBAL_TOUCH_CURRENT_POSITION_Y = 0;
                    break;

                case MotionEvent.ACTION_MOVE:

                    if (event.getPointerCount() >=fingerCount) {
                        GLOBAL_TOUCH_CURRENT_POSITION_X = (int) event.getX(fingerCount-1);

                        GLOBAL_TOUCH_CURRENT_POSITION_Y = (int) event.getY(fingerCount-1);

                        updateViews();
                    }
                    int diff = GLOBAL_TOUCH_POSITION_X-GLOBAL_TOUCH_CURRENT_POSITION_X;

                    if(Math.abs(diff)>=500) {
                        actionStr = "it is a " + fingerCount + " finger drag "+calculateDistance();
                        textView.setText(actionStr);
                        flag = true;
                    }
                    else{
                        actionStr = "";
                        textView.setText(actionStr);
                        flag = false;
                    }
                    break;

                default:
                    actionStr = "";
                    textView.setText(actionStr);
            }
            count = 0;
        }

        return flag;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int count =event.getPointerCount();

        handleMultiTouch(event, count);

        return super.onTouchEvent(event);
    }

    private void updateStatus(String str) {
        textView.setText(str);
    }

    private void updateViews() {
        currentXTextView.setText("current x: "+String.valueOf(GLOBAL_TOUCH_CURRENT_POSITION_X));
        currentYTextView.setText("current y: "+String.valueOf(GLOBAL_TOUCH_CURRENT_POSITION_Y));
        originalXTextView.setText("original x: "+String.valueOf(GLOBAL_TOUCH_POSITION_X));
        originalYTextView.setText("original y: "+String.valueOf(GLOBAL_TOUCH_POSITION_Y));
    }

    private double calculateDistance() {
        double vertical = Math.abs(GLOBAL_TOUCH_CURRENT_POSITION_X - GLOBAL_TOUCH_POSITION_X);
        double horizontal = Math.abs(GLOBAL_TOUCH_CURRENT_POSITION_Y - GLOBAL_TOUCH_POSITION_Y);

        return Math.sqrt(vertical*vertical + horizontal*horizontal);
    }
}
