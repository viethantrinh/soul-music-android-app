package net.branium.service;

import static net.branium.service.ApplicationClass.ACTION_NEXT;
import static net.branium.service.ApplicationClass.ACTION_PLAY;
import static net.branium.service.ApplicationClass.ACTION_PREVIOUS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String actionName = intent.getAction();
        Intent servicenIntent = new Intent(context, MusicService.class);
        if(actionName != null) {
            switch (actionName) {
                case ACTION_PLAY -> {
                    servicenIntent.putExtra("ActionName", "playPause");
                    context.startService(servicenIntent);
                }
                case ACTION_NEXT -> {
                    servicenIntent.putExtra("ActionName", "next");
                    context.startService(servicenIntent);
                }
                case ACTION_PREVIOUS -> {
                    servicenIntent.putExtra("ActionName", "previous");
                    context.startService(servicenIntent);
                }
            }
        }
    }
}
