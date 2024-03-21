package net.branium.view.services;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import net.branium.utils.Notification;

public class MusicService extends Service{
    IBinder myBinder = new MyBinder();
    MusicAction musicAction;
    Notification notification;

    @Override
    public void onCreate() {
        super.onCreate();
        notification = new Notification(this);

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String actionName = intent.getStringExtra("ActionName");
            if (actionName != null) {
                switch (actionName) {
                    case "playPause" -> {
                        if (musicAction != null) {
                            musicAction.playPause();
                        }
                    }
                    case "next" -> {
                        if (musicAction != null) {
                            musicAction.skipNext();
                        }
                    }
                    case "previous" -> {
                        if (musicAction != null) {
                            musicAction.skipPrevious();
                        }
                    }
                }
            }
        }
        return START_STICKY;
    }

    public void setCallBack(MusicAction musicAction) {
        this.musicAction = musicAction;
    }
}
