package br.com.food4fit.broadcast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import br.com.food4fit.MainActivity;
import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.RefeicaoEntity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "FOOD4FIT_NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        int idRefeicao = intent.getIntExtra("refeicao", -1);
        if (idRefeicao != -1) {
            RefeicaoEntity refeicao = AppDatabase.getDatabase(context).getRefeicaoDAO().selectData(idRefeicao);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                Intent resultIntent = new Intent(context, MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification notification =
                        new NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.drawable.logo_4fit_simple)
                                .setContentTitle("É hora de comer!")
                                .setContentText(refeicao.getHorario() + " - " + refeicao.getTitulo())
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setContentIntent(resultPendingIntent)
                                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                .setAutoCancel(true)
                                .setChannelId(CHANNEL_ID)
                                .build();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "Food4FIT", NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(mChannel);
                }

                notificationManager.notify(idRefeicao, notification);
            }
        }
    }
}
