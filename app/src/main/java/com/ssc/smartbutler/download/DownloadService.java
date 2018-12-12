package com.ssc.smartbutler.download;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.ShareUtil;

import java.io.File;

public class DownloadService extends Service {

    private static final String TAG = "DownloadService";

    private DownloadTask downloadTask;//异步下载任务

    private String downloadUrl;

    //回调接口
    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            //更新通知进度
            //getNotificationManager().notify(1, getNotification("Downloading...", progress));
            getNotification("下载中...", progress, false);
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            //下载成功时将前台服务通知关闭,并创建一个下载成功的通知
            stopForeground(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = isHasInstallPermissionWithO();
                if (!hasInstallPermission) {
                    //startInstallPermissionSettingActivity(context);
                }else {

                }
                getNotification("下载完成。点击安装", -1, false);
                Toast.makeText(DownloadService.this, "下载完成。点击通知安装", Toast.LENGTH_SHORT).show();
                sendBroadcast(new Intent("com.ssc.smartbutler.APKReceiver"));
            } else {
                //getNotificationManager().notify(1, getNotification("Download Success", -1));
                getNotification("下载完成。点击安装", -1, false);
                Toast.makeText(DownloadService.this, "下载完成。点击通知安装", Toast.LENGTH_SHORT).show();
                sendBroadcast(new Intent("com.ssc.smartbutler.APKReceiver"));
                L.i("haha","通知");
            }
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            //下载失败时将前台服务通知关闭,并创建一个下载失败的通知
            stopForeground(true);
            //getNotificationManager().notify(1, getNotification("Download Failed", -1));
            getNotification("Download Failed", -1, false);
            Toast.makeText(DownloadService.this, "下载失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "暂停", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            //下载取消时将前台服务通知关闭
            stopForeground(true);
            Toast.makeText(DownloadService.this, "取消", Toast.LENGTH_SHORT).show();
        }
    };

    private DownloadBinder mBinder = new DownloadBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //activity与service通信
    public class DownloadBinder extends Binder {

        public DownloadService getDownloadService() {
            return DownloadService.this;
        }

        public void startDownload(String url) {
            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl);//启动下载任务
                //让DownloadService变成前台服务
                //startForeground(1, getNotification("Downloading...", 0));
                getNotification("下载中...", 0, true);
                Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                //正在下载,下载还未完成(在DownloadTask中如果取消下载,已经有了文件删除的操作,所以此处不需要)
                downloadTask.cancelDownload();
            } else {
                //下载已经完成,或暂停,或遇到问题下载失败等
                if (downloadUrl != null) {
                    //取消下载时需将文件删除,并将前台服务通知关闭
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    //移除通知(包括下载失败等通知)
                    getNotificationManager().cancel(1);
                    //将前台服务通知(下载进度的通知)关闭
                    stopForeground(true);
                    Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void getNotification(String title, int progress, boolean isForeground) {
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        /*Intent intent = new Intent(this, UpdateActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);*/
        //Notification notification = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = "my_channel_01";
            String name = "channel_name";
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{0,1000, 1000, 1000});//设置震动模式
            notificationManager.createNotificationChannel(mChannel);
            Notification.Builder builder = new Notification.Builder(this, "default");
            builder.setChannelId(id);
            getNotif_O(title, progress, isForeground, builder, notificationManager);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            getNotif(title, progress, isForeground, builder, notificationManager);
        }


    }

    public void getNotif_O(String title, int progress, boolean isForeground, Notification.Builder builder, NotificationManager notificationManager) {
        if (title.equals("下载完成。点击安装")) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            String path = Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS).getPath() + downloadUrl.substring(downloadUrl.lastIndexOf("/")) + ".apk";//内置sd卡的Download文件夹下
            if (Build.VERSION.SDK_INT >= 24) {
                File file = new File(path);
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri =
                        FileProvider.getUriForFile(this, "com.ssc.smartbutler.fileprovider", file);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.setDataAndType(apkUri, "application/vnd.android.package-archive");

            } else {
                i.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            }
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
            builder.setContentIntent(pi);
        }
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        //builder.setVibrate(new long[]{0, 1000,1000, 1000});
        builder.setContentTitle(title);
        if (progress >= 0) {
            //当progress大于或等于0时才需显示下载进度
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        if (isForeground) {
            //前台服务
            startForeground(1, builder.build());
        } else {
            //普通通知
            notificationManager.notify(1, builder.build());
        }
    }

    public void getNotif(String title, int progress, boolean isForeground, NotificationCompat.Builder builder, NotificationManager notificationManager) {
        if (title.equals("下载完成。点击安装")) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            String path = Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS).getPath() + downloadUrl.substring(downloadUrl.lastIndexOf("/")) + ".apk";//内置sd卡的Download文件夹下
            if (Build.VERSION.SDK_INT >= 24) {
                File file = new File(path);
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri =
                        FileProvider.getUriForFile(DownloadService.this, "com.ssc.smartbutler.fileprovider", file);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.setDataAndType(apkUri, "application/vnd.android.package-archive");
                L.i("hahaha","文件");
            } else {
                i.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            }
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
            builder.setContentIntent(pi);
            builder.setVibrate(new long[]{0, 1000, 1000, 1000});
        }
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        if (progress >= 0) {
            //当progress大于或等于0时才需显示下载进度
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        if (isForeground) {
            //前台服务
            startForeground(1, builder.build());
        } else {
            //普通通知
            notificationManager.notify(1, builder.build());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    /**
     * 如果为8.0以上系统，则判断是否有未知应用安装权限
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO() {
        return getPackageManager().canRequestPackageInstalls();
    }


}
