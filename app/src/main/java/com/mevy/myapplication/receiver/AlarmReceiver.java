package com.mevy.myapplication.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class AlarmReceiver extends BroadcastReceiver {
    public static Ringtone ringtone;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    // implement onReceive() method
    public void onReceive(Context context, Intent intent) {
        // Create a notification manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if Android version is Oreo or higher, then create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        String namaFilm = intent.getStringExtra("nama_film");

        // Build the notification
        Notification.Builder builder = new Notification.Builder(context, "default")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(namaFilm)
                .setContentText("Film akan segera dimulai!")
                .setAutoCancel(false) // Set to false to prevent the notification from being dismissed
                .setOngoing(true); // Set ongoing to true to prevent dismissal

        // Create an Intent to handle the action of disabling the alarm
        Intent disableIntent = new Intent(context, DisableAlarmReceiver.class);
        PendingIntent pendingDisableIntent = PendingIntent.getBroadcast(context, 0, disableIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Add action to the notification
        builder.addAction(android.R.drawable.ic_lock_power_off, "OK", pendingDisableIntent);

        // Show the notification
        notificationManager.notify(0, builder.build());

        // Vibrate
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(4000, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(4000);
            }
        }

        // Play alarm sound
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        // Show toast for debugging purposes
        Toast.makeText(context, "Film " + namaFilm + " akan segera dimulai!", Toast.LENGTH_LONG).show();
    }
}
