package beini.com.fordingding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by beini on 2018/1/12.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("com.beini", "--------->onReceive   action=" + action);
        if ("ALARM_TEMP".equals(action)) {
            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
            final String pageName = "com.alibaba.android.rimet";
            PackageManager packageManager = context.getPackageManager();
            Intent intentStart = packageManager.getLaunchIntentForPackage(pageName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(intentStart);
        }
    }
}
