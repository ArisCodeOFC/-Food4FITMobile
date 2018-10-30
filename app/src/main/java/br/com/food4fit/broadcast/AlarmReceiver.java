package br.com.food4fit.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Don't panik but your time is up!!!!.",Toast.LENGTH_LONG).show();
        Log.d("teste", "undawuiduiawuduahwd");
    }
}
