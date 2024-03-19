package net.branium.view.activities;

import static net.branium.utils.Constants.ACTION_NEXT;
import static net.branium.utils.Constants.ACTION_PLAY;
import static net.branium.utils.Constants.ACTION_PREVIOUS;
import static net.branium.utils.Constants.CHANNEL_ID_2;
import static net.branium.utils.Constants.PLAYLIST_SONG_LIST;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import net.branium.R;
import net.branium.databinding.ActivityMusicPlayerBinding;
import net.branium.model.Song;
import net.branium.utils.Constants;
import net.branium.view.fragments.main.MiniPlayerFragment;
import net.branium.view.services.MusicAction;
import net.branium.view.services.MusicService;
import net.branium.view.services.NotificationReceiver;

import java.util.Random;

@FunctionalInterface
interface OnThreadClickListener {
    void action();
}

public class MusicActivity extends AppCompatActivity implements MusicAction, ServiceConnection{
    private static int position = -1;
    private static Uri uri;
    public static MediaPlayer mediaPlayer;
    public static boolean isShuffle = false;
    public static boolean isRepeat = false;
    private ActivityMusicPlayerBinding binding;
    private Handler handler = new Handler();
    MusicService musicService;
    MediaSessionCompat mediaSessionCompat;
    MiniPlayerFragment miniPlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_music_player);
        setupMediaPlayer();
        handleEventListener();
        mediaSessionCompat = new MediaSessionCompat(this, "My Audio");
        showNotification(R.drawable.ic_pause_24);

    }

    private void handleEventListener() {
        binding.seekBarMusicPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        MusicActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int musicCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    binding.seekBarMusicPlayer.setProgress(musicCurrentPosition);
                    binding.tvMusicPlayerDurationRun.setText(getFormattedDuration(musicCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });

        mediaPlayer.setOnCompletionListener(mp -> skipNext());

        binding.ivMusicPlayerShuffle.setOnClickListener(v -> {
            if (isShuffle) {
                binding.ivMusicPlayerShuffle.setImageResource(R.drawable.ic_shuffle_off_24);
            } else {
                binding.ivMusicPlayerShuffle.setImageResource(R.drawable.ic_shuffle_on_24);
            }
            isShuffle = !isShuffle;
        });

        binding.ivMusicPlayerRepeat.setOnClickListener(v -> {
            if (isRepeat) {
                binding.ivMusicPlayerRepeat.setImageResource(R.drawable.ic_repeat_off_24);
            } else {
                binding.ivMusicPlayerRepeat.setImageResource(R.drawable.ic_repeat_on_24);
            }
            isRepeat = !isRepeat;
        });

        binding.ivMusicPlayerCloseDown.setOnClickListener(v -> {
//            if (mediaPlayer != null) {
//                mediaPlayer.stop();
//                mediaPlayer.release();
//                mediaPlayer = null;
//            }
            finish();
        });
    }

    private String getFormattedDuration(int musicCurrentPosition) {
        String seconds = String.valueOf(musicCurrentPosition % 60);
        String minutes = String.valueOf(musicCurrentPosition / 60);
        if (seconds.length() == 1) {
            return minutes + ":" + "0" + seconds;
        } else {
            return minutes + ":" + seconds;
        }
    }

    private void setupMediaPlayer() {
        position = getIntent().getIntExtra("position", -1);
        Song currentSong = Constants.PLAYLIST_SONG_LIST.get(position);
        setUpSongData(currentSong);
        binding.fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
        if (mediaPlayer != null) { // trong trường hợp media player tồn tại bài hát -> stop -> release -> chạy lại
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
    }

    private void setUpSongData(Song currentSong) {
        binding.tvMusicPlayerTitle.setText(currentSong.getTitle());
        binding.tvMusicPlayerArtist.setText(currentSong.getArtist());
        Glide.with(getApplicationContext()).load(currentSong.getImage()).into(binding.ivMusicPlayerPhoto);
        binding.tvMusicPlayerDurationTotal.setText(getFormattedDuration(currentSong.getDuration()));
        binding.seekBarMusicPlayer.setMax(currentSong.getDuration());
        uri = Uri.parse(currentSong.getSource());

        Constants.MINI_PLAYER_ACTIVE = true;
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARE_PREFERENCE_KEY, MODE_PRIVATE).edit();
        editor.putString(Constants.MUSIC_TITLE, currentSong.getTitle());
        editor.putString(Constants.MUSIC_ARTIST, currentSong.getArtist());
        editor.putString(Constants.MUSIC_IMAGE, currentSong.getImage());
        editor.putString(Constants.MUSIC_SOURCE, uri.toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        // bind music service
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);

        setUpThread(binding.fltBtnMusicPlayerPlayPause, this::playPause);
        setUpThread(binding.ivMusicPlayerSkipNext, this::skipNext);
        setUpThread(binding.ivMusicPlayerSkipPrevious, this::skipPrevious);

        showNotification(R.drawable.ic_pause_24);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    private void setUpThread(View view, OnThreadClickListener onThreadClickListener) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onThreadClickListener.action();
                    }
                });
            }
        };
        thread.start();
    }

    @Override
    public void playPause() {
        if (mediaPlayer.isPlaying()) {
            showNotification(R.drawable.ic_play_24);
            binding.fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_play_24);
            mediaPlayer.pause();
        } else {
            showNotification(R.drawable.ic_pause_24);
            binding.fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
            mediaPlayer.start();
        }
    }

    @Override
    public void skipPrevious() {
        mediaPlayer.stop();
        mediaPlayer.release();
        if (!isShuffle && !isRepeat) {
            position = (position - 1 < 0) ? (Constants.PLAYLIST_SONG_LIST.size() - 1) : (position - 1);
        } else if (isShuffle && !isRepeat) {
            position = new Random().nextInt(Constants.PLAYLIST_SONG_LIST.size());
        } // (!isShuffle && isRepeat) or (isShuffle && isRepeat) => position = currentPosition
        setUpSongData(Constants.PLAYLIST_SONG_LIST.get(position));
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);

        showNotification(R.drawable.ic_pause_24);
        binding.fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
        mediaPlayer.setOnCompletionListener(mp -> skipNext());
        mediaPlayer.start();
    }

    @Override
    public void skipNext() {
        mediaPlayer.stop();
        mediaPlayer.release();
        if (!isShuffle && !isRepeat) {
            position = ((position + 1) % Constants.PLAYLIST_SONG_LIST.size());
        } else if (isShuffle && !isRepeat) {
            position = new Random().nextInt(Constants.PLAYLIST_SONG_LIST.size());
        } // (!isShuffle && isRepeat) or (isShuffle && isRepeat) => position = currentPosition
        setUpSongData(Constants.PLAYLIST_SONG_LIST.get(position));
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);

        showNotification(R.drawable.ic_pause_24);
        binding.fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
        mediaPlayer.setOnCompletionListener(mp -> skipNext());
        mediaPlayer.start();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        musicService.setCallBack(this);
        Toast.makeText(this, "Connected" + musicService, Toast.LENGTH_SHORT).show();
        binding.seekBarMusicPlayer.setMax(mediaPlayer.getDuration() / 1000);

    }
    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }

    public void showNotification(int playPauseBtn) {
        Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent
                .getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent
                .getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent
                .getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE);

        String imageString = PLAYLIST_SONG_LIST.get(position).getImage();

        // Load the WebP image into a Bitmap using Glide
        Glide.with(this)
                .asBitmap()
                .load(imageString)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Set the Bitmap as the large icon for the notification
                        createNotification(resource, prevPending, playPauseBtn, pausePending, nextPending);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void createNotification(@NonNull Bitmap resource, PendingIntent prevPending, int playPauseBtn, PendingIntent pausePending, PendingIntent nextPending) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_2)
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
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification.build());
    }
}