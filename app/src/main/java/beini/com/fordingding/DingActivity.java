package beini.com.fordingding;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DingActivity extends Activity {
    private TimePickerView pvTime;
    private CheckBox cb_repeat;
    private boolean isCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initJobScheduler();
        initAlarmManager();
    }


    private void initAlarmManager() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE));
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019, 11, 28);
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                Button btn = (Button) v;
                btn.setText(getTime(date));
                setAlarmManager(date);
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN)
                .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.DKGRAY)
                .setContentSize(20)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .build();
        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show(v);
            }
        });
        cb_repeat = findViewById(R.id.cb_repeat);
        cb_repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck = isChecked;
            }
        });
    }

    public void setAlarmManager(Date date) {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.setAction("ALARM_TEMP");
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);

        //格式转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateStrDate = sdf.format(new Date());
        String dateStrSetting = sdf.format(date);
        Log.e("com.beini", "------------>" + dateStrDate + "       " + dateStrSetting);

        if (isCheck) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            for (int i = 1; i <= 7; i++) {
                PendingIntent pIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                calendar.set(Calendar.DAY_OF_WEEK, i);
                Log.e("com.beini", "     i=" + i + "   sdf.format(new Date())=" + sdf.format(calendar.getTime()));
                alarmMgr.setWindow(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 2000, pIntent);
            }
        } else {
            PendingIntent pIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.setWindow(AlarmManager.RTC_WAKEUP, date.getTime(), 2000, pIntent);
            Log.e("com.beini", "   sdf.format(new Date())=" + sdf.format(date));
        }

        Toast.makeText(this, "setting success", Toast.LENGTH_SHORT).show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    private void initJobScheduler() {
        final ComponentName jobService;
        jobService = new ComponentName(this, TimingService.class);
        Intent service = new Intent(this, TimingService.class);
        startService(service);
        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobScheduler jobScheduler = (JobScheduler)
                        getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo jobInfo = new JobInfo.Builder(1, jobService)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) //任何有网络的状态
                        .setPeriodic(3000)
                        .setRequiresDeviceIdle(false)
                        .build();
                int result = jobScheduler.schedule(jobInfo);
                Log.e("com.beini", "result=" + result);
            }
        });
    }
}
