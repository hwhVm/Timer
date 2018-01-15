package beini.com.fordingding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends Activity {
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.btn_ding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, DingActivity.class));
            }
        });
        timer = new Timer();
        findViewById(R.id.btn_timer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Log.e("com.beini", "--------->cancel");
//                        timer.cancel();
                    }
                };
                //3秒后执行一次
//                timer.schedule(timerTask, 3000);
                // 1s执行一次
//                timer.schedule(timerTask, 1000, 1000);
                //执行指定的时间，如果是过去的时间就立刻执行
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, 2);
//              timer.schedule(timerTask, calendar.getTime());
                //执行指定的时间,period重复执行的时间
                timer.schedule(timerTask, calendar.getTime(), 2000);
            }
        });
        findViewById(R.id.btn_timer_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.purge();
            }
        });
    }
}
