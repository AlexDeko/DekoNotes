package com.app.dekonotes.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.app.dekonotes.R;
import com.app.dekonotes.activity.EnterPinActivity;
import com.app.dekonotes.activity.MainActivity;
import com.app.dekonotes.data.note.Note;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.telephony.AvailableNetworkInfo.PRIORITY_HIGH;

public class ServiceNotification extends Service {
    private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static boolean checkDeadlineEnd = false;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);


    }

    void sendNotification(){
        Intent intent = new Intent(getApplicationContext(), EnterPinActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat
                .Builder(getApplicationContext(), CHANNEL_ID)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_notepad_12)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setContentTitle(getString(R.string.notification_deadline_end_title))
                .setContentText(getString(R.string.notification_deadline_end_text))
                .setPriority(PRIORITY_HIGH);

        createChannelIfNeeded(notificationManager);
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw new UnsupportedOperationException("Not yet implemented");

    }

    public void createChannelIfNeeded(NotificationManager manager) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);

            manager.createNotificationChannel(notificationChannel);

        }
    }




    public static void checkList(List<Note> notes){
        if (notes != null){
            for (int i = 0; i < notes.size(); i++){
                Note note =  notes.get(i);
                if (note.getDayDeadline() < new Date().getTime()) {
                    checkDeadlineEnd = true;
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        notificationManager = (NotificationManager) getApplicationContext()
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//
//        intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
//                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat
//                .Builder(getApplicationContext(), CHANNEL_ID)
//                .setAutoCancel(false)
//                .setSmallIcon(R.drawable.ic_notepad_12)
//                .setWhen(System.currentTimeMillis())
//                .setContentIntent(pendingIntent)
//                .setContentTitle(getString(R.string.notification_deadline_end_title))
//                .setContentText(getString(R.string.notification_deadline_end_text))
//                .setPriority(PRIORITY_HIGH);
//
//        createChannelIfNeeded(notificationManager);
//        notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
//
        if ((flags & START_FLAG_RETRY) == 0) {
            // TODO Если это повторный запуск, выполнить какие-то действия.
            checkDeadlineEnd = false;
        }
        else {

            // TODO Альтернативные действия в фоновом режиме.
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (checkDeadlineEnd){
            sendNotification();
        }

        return Service.START_STICKY;
    }
}
