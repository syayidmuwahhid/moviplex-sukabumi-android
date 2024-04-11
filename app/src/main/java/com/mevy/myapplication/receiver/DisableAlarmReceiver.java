package com.mevy.myapplication.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DisableAlarmReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        // Hentikan alarm di sini
        // Anda dapat menggunakan AlarmManager untuk membatalkan PendingIntent yang terkait dengan alarm

        // Misalnya, jika Anda ingin membatalkan PendingIntent yang terkait dengan alarm, Anda dapat melakukannya seperti ini:
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        // Hentikan suara alarm jika ada
        if (AlarmReceiver.ringtone != null && AlarmReceiver.ringtone.isPlaying()) {
            AlarmReceiver.ringtone.stop();
        }

        // Tampilkan pesan bahwa alarm telah dinonaktifkan
//        Toast.makeText(context, "Alarm disabled", Toast.LENGTH_SHORT).show();

        // Hapus notifikasi
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }
}
