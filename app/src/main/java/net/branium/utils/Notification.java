package net.branium.utils;

import static net.branium.utils.Constants.ACTION_NEXT;
import static net.branium.utils.Constants.ACTION_PLAY;
import static net.branium.utils.Constants.ACTION_PREVIOUS;
import static net.branium.utils.Constants.CHANNEL_ID_2;
import static net.branium.utils.Constants.PLAYLIST_SONG_LIST;
import static net.branium.view.activities.MusicActivity.position;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import net.branium.R;
import net.branium.view.services.NotificationReceiver;

import java.util.Date;

public class Notification {

    public Notification(Context context) {
    }

    public static void showNotification(Context context, int playPauseBtn) {
        Intent prevIntent = new Intent(context, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent
                .getBroadcast(context, 0, prevIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent pauseIntent = new Intent(context, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent
                .getBroadcast(context, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(context, NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent
                .getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE);

        String imageString = PLAYLIST_SONG_LIST.get(position).getImage();

        // Load the WebP image into a Bitmap using Glide
        Glide.with(context)
                .asBitmap()
                .load(imageString)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Set the Bitmap as the large icon for the notification
                        createNotification(context, resource, prevPending, playPauseBtn, pausePending, nextPending);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private static void createNotification(Context context, @NonNull Bitmap resource, PendingIntent prevPending, int playPauseBtn, PendingIntent pausePending, PendingIntent nextPending) {
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "My Audio");
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID_2)
                .setSmallIcon(R.drawable.img_logo)
                .setLargeIcon(resource)
                .setContentTitle(PLAYLIST_SONG_LIST.get(position).getTitle())
                .setContentText(PLAYLIST_SONG_LIST.get(position).getArtist())
                .addAction(R.drawable.ic_skip_previous_24, "Previous", prevPending)
                .addAction(playPauseBtn, "Pause", pausePending)
                .addAction(R.drawable.ic_skip_next_24, "Next", nextPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1 /* #1: pause button */)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_LOW);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification.build());
    }
}
