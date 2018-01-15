package beini.com.fordingding;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by beini on 2018/1/12.
 * 当设备接通电源适配器
 * 连接到WIFI
 *
 */

public class TimingService extends JobService {
    @Override
    public void onCreate() {
        Log.e("com.beini", "  onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("com.beini", "---->onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * fase:已经被执行，true：正在执行
     *
     * @param params
     * @return
     */
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("com.beini", "------onStartJob");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e("com.beini", "------onStopJob");
        return false;
    }
}
