package com.firebasepushnotification.kaushik.firebasepushnotification.utill;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.firebasepushnotification.kaushik.firebasepushnotification.R;
import com.firebasepushnotification.kaushik.firebasepushnotification.activity.MainActivity;
import com.firebasepushnotification.kaushik.firebasepushnotification.app.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class NotificationUtills {


    private static final int PENDING_INTENT_ID = 3417;
    private static final int NOTIFICATION_ID = 1138;
    private static final String NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";



    public static void showSmallNotification(Context context, String title, String message, String timeStamp) {
         NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(contentIntent(context))
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID, notification);
    }
    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

  public static void testNotification(Context context)
     {
         NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Testing", NotificationManager.IMPORTANCE_HIGH);
    notificationManager.createNotificationChannel(mChannel);
}


         NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID).setColor(ContextCompat.getColor(context, R.color.colorPrimary)).setContentTitle("Title")
                                                                                .setContentText("Body")
                 .setSmallIcon(R.drawable.ic_launcher_foreground)
                                                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                                                          "Shake your booty"))
                                            .setDefaults(Notification.DEFAULT_VIBRATE)
                                                    .setContentIntent(contentIntent(context))
                                                            .setAutoCancel(true);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                 && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
             notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
         }

         notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
     }

    private static PendingIntent contentIntent(Context context)
    {
        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context,PENDING_INTENT_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);

    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}
