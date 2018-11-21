package br.com.food4fit.broadcast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Compra;

public class CompraAlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "FOOD4FIT_NOTIFICATION_BUY";

    @Override
    public void onReceive(Context context, Intent intent) {
         int idOrdemServico = intent.getIntExtra("id_ordem_servico", -1);
         int idPrato = intent.getIntExtra("id_prato", -1);

         if (idOrdemServico != -1 && idPrato != -1) {
             Compra compra = AppDatabase.getDatabase(context).getCompraDAO().selecionar(idPrato, idOrdemServico);
             if (compra != null) {
                 NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                 if (notificationManager != null) {
                     Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.food4fit.com.br/"));
                     TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                     stackBuilder.addNextIntentWithParentStack(resultIntent);
                     PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                     Notification notification =
                             new NotificationCompat.Builder(context, CHANNEL_ID)
                                     .setSmallIcon(R.drawable.logo_4fit_simple)
                                     .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_4fit_simple))
                                     .setContentTitle(compra.getTitulo())
                                     .setContentText("Sua compra se esgotou. Deseja comprar novamente?")
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

                     notificationManager.notify(idPrato, notification);
                 }

                 AppDatabase.getDatabase(context).getCompraDAO().remove(compra);
             }
         }
    }
}
