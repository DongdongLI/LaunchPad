package com.launchpad.dli.launchpad;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.launchpad.dli.launchpad.fragment.ConfigDialog;

public class MainActivity extends AppCompatActivity {

    final static String DEBUG_TAG = "mainactivity";
    public static final String PREFS_NAME = "mypreference";

    TextView textView;

    TextView originalXTextView;
    TextView originalYTextView;
    TextView currentXTextView;
    TextView currentYTextView;

    static int GLOBAL_TOUCH_POSITION_X = 0;
    static int GLOBAL_TOUCH_CURRENT_POSITION_X = 0;

    static int GLOBAL_TOUCH_POSITION_Y = 0;
    static int GLOBAL_TOUCH_CURRENT_POSITION_Y = 0;

    static String twoFingerDragConfig;
    static String threeFingerDragConfig;
    static String fourFingerDragConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.statusText);

        originalXTextView = (TextView) findViewById(R.id.originalX);
        originalYTextView = (TextView) findViewById(R.id.originalY);
        currentXTextView = (TextView) findViewById(R.id.currentX);
        currentYTextView = (TextView) findViewById(R.id.currentY);

        refreshConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshConfig();
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

                        switch (fingerCount){
                            case 2:
                                String url = "waze://?ll=27.3969313,-82.4425731&navigate=yes";
                                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                                startActivity( intent );
                                break;
                            case 3:
                                openApp(this, threeFingerDragConfig);
                                break;
                            case 4:
                                openApp(this, fourFingerDragConfig);
                                break;
                            default:
                                break;
                        }
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

    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        if (i == null) {
            Log.d(DEBUG_TAG, "nothing");
            return false;
        }
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(i);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int count =event.getPointerCount();

        handleMultiTouch(event, count);

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item1:
                //Toast.makeText(this, "Config", Toast.LENGTH_SHORT).show();
                showDialog();
                return true;

            case R.id.menu_item2:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

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

    public void showDialog() {
        DialogFragment newFragment = new ConfigDialog();
        newFragment.show(getFragmentManager(), "Config");
    }

    private void refreshConfig() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);

        twoFingerDragConfig = sharedPreferences.getString("2FingerDrag","empty value");
        threeFingerDragConfig = sharedPreferences.getString("3FingerDrag","empty value");
        fourFingerDragConfig = sharedPreferences.getString("4FingerDrag","empty value");
    }

}
