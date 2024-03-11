package net.branium.view.activities;

import static net.branium.service.ApplicationClass.ACTION_NEXT;
import static net.branium.service.ApplicationClass.ACTION_PLAY;
import static net.branium.service.ApplicationClass.ACTION_PREVIOUS;
import static net.branium.service.ApplicationClass.CHANNEL_ID_2;
import static net.branium.view.activities.SplashActivity.repeatBoolean;
import static net.branium.view.activities.SplashActivity.shuffleBoolean;
import static net.branium.view.adapters.MusicAdapter.songList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.branium.R;
import net.branium.model.Song;
import net.branium.service.ActionPlayingService;
import net.branium.service.MusicService;
import net.branium.service.NotificationReceiver;
import net.branium.view.fragments.main.PlaylistFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicPlayerActivity extends AppCompatActivity
        implements ActionPlayingService, ServiceConnection {
    private ImageView ivMusicPlayerCloseDown;
    private ImageView ivMusicPlayerOption;
    private ImageView ivMusicPlayerPhoto;
    private TextView tvMusicPlayerTitle;
    private TextView tvMusicPlayerArtist;
    private TextView tvMusicPlayerDurationRun;
    private TextView tvMusicPlayerDurationTotal;
    private SeekBar seekBarMusicPlayer;
    private ImageView ivMusicPlayerShuffle;
    private ImageView ivMusicPlayerRepeat;
    private ImageView ivMusicPlayerSkipPrevious;
    private ImageView ivMusicPlayerSkipNext;
    private FloatingActionButton fltBtnMusicPlayerPlayPause;
    private int position = -1;
    public static List<Song> listSongs = new ArrayList<>();
    public static Uri uri;
//    public static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playPauseThread;
    private Thread skipPreviousThread;
    private Thread skipNextThread;
    MusicService musicService;
    MediaSession mediaSessionCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        mediaSessionCompat = new MediaSession(getBaseContext(), "My Audio");
        initializeViewComponent();
        try {
            getIntentMethod();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        seekBarMusicPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicService != null && fromUser) {
                    musicService.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    seekBarMusicPlayer.setProgress(mCurrentPosition);
                    tvMusicPlayerDurationRun.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });

        ivMusicPlayerShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffleBoolean) {
                    shuffleBoolean = false;
                    ivMusicPlayerShuffle.setImageResource(R.drawable.ic_shuffle_off_24);
                } else {
                    shuffleBoolean = true;
                    ivMusicPlayerShuffle.setImageResource(R.drawable.ic_shuffle_on_24);
                }
            }
        });

        ivMusicPlayerRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatBoolean) {
                    repeatBoolean = false;
                    ivMusicPlayerRepeat.setImageResource(R.drawable.ic_repeat_off_24);
                } else {
                    repeatBoolean = true;
                    ivMusicPlayerRepeat.setImageResource(R.drawable.ic_repeat_on_24);
                }
            }
        });
        ivMusicPlayerCloseDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        playPauseThreadBtn();
        skipPreviousThreadBtn();
        skipNextThreadBtn();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    private void playPauseThreadBtn() {
        playPauseThread = new Thread() {
            @Override
            public void run() {
                super.run();
                fltBtnMusicPlayerPlayPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            playPauseBtnClicked();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        };
        playPauseThread.start();
    }
    private void skipNextThreadBtn() {
        skipNextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                ivMusicPlayerSkipNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            skipNextBtnClicked();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        };
        skipNextThread.start();
    }
    private void skipPreviousThreadBtn() {
        skipPreviousThread = new Thread() {
            @Override
            public void run() {
                super.run();
                ivMusicPlayerSkipPrevious.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            skipPreviousBtnClicked();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        };
        skipPreviousThread.start();

    }

    public void playPauseBtnClicked() throws IOException {
        if (musicService.isPlaying()) {
            fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_play_24);
            showNotification(R.drawable.ic_play_24);
            musicService.pause();
            seekBarMusicPlayer.setMax(musicService.getDuration() / 1000);
            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBarMusicPlayer.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        } else {
            showNotification(R.drawable.ic_pause_24);
            fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
            musicService.start();
            seekBarMusicPlayer.setMax(musicService.getDuration() / 1000);
            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBarMusicPlayer.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }
    public void skipNextBtnClicked() throws IOException {
        if(musicService != null) {
            if (musicService.isPlaying()) {
                musicService.stop();
                musicService.release();
                if (shuffleBoolean && !repeatBoolean) {
                    position = getRandom(listSongs.size() - 1);
                } else if (!shuffleBoolean && !repeatBoolean) {
                    position = ((position + 1) % listSongs.size());
                } else {
                    // position is position
                }
                uri = Uri.parse(listSongs.get(position).getPath());
                musicService.createMediaPlayer(position);
                metaData(uri);
                tvMusicPlayerTitle.setText(listSongs.get(position).getTitle());
                tvMusicPlayerArtist.setText(listSongs.get(position).getArtist());
                seekBarMusicPlayer.setMax(musicService.getDuration() / 1000);
                MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (musicService != null) {
                            int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                            seekBarMusicPlayer.setProgress(mCurrentPosition);
                        }
                        handler.postDelayed(this, 1000);
                    }
                });
                musicService.OnCompleted();
                showNotification(R.drawable.ic_pause_24);
                fltBtnMusicPlayerPlayPause.setBackgroundResource(R.drawable.ic_pause_24);
                musicService.start();
            } else {
                musicService.stop();
                musicService.release();
                if (shuffleBoolean && !repeatBoolean) {
                    position = getRandom(listSongs.size() - 1);
                } else if (!shuffleBoolean && !repeatBoolean) {
                    position = ((position + 1) % listSongs.size());
                } else {
                    // position is position
                }
                position = ((position + 1) % listSongs.size());
                uri = Uri.parse(listSongs.get(position).getPath());
                musicService.createMediaPlayer(position);
                metaData(uri);
                tvMusicPlayerTitle.setText(listSongs.get(position).getTitle());
                tvMusicPlayerArtist.setText(listSongs.get(position).getArtist());
                seekBarMusicPlayer.setMax(musicService.getDuration() / 1000);
                MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (musicService != null) {
                            int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                            seekBarMusicPlayer.setProgress(mCurrentPosition);
                        }
                        handler.postDelayed(this, 1000);
                    }
                });
                musicService.OnCompleted();
                showNotification(R.drawable.ic_play_24);
                fltBtnMusicPlayerPlayPause.setBackgroundResource(R.drawable.ic_play_24);
            }
        }
    }
    public void skipPreviousBtnClicked() throws IOException {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = (position - 1) < 0 ? (listSongs.size() - 1) : (position - 1);
            } else {
                // position is position
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            tvMusicPlayerTitle.setText(listSongs.get(position).getTitle());
            tvMusicPlayerArtist.setText(listSongs.get(position).getArtist());
            seekBarMusicPlayer.setMax(musicService.getDuration() / 1000);
            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBarMusicPlayer.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });

            musicService.OnCompleted();
            showNotification(R.drawable.ic_pause_24);
            fltBtnMusicPlayerPlayPause.setBackgroundResource(R.drawable.ic_pause_24);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSongs.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = (position - 1) < 0 ? (listSongs.size() - 1) : (position - 1);
            } else {
                // position is position
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            tvMusicPlayerTitle.setText(listSongs.get(position).getTitle());
            tvMusicPlayerArtist.setText(listSongs.get(position).getArtist());
            seekBarMusicPlayer.setMax(musicService.getDuration() / 1000);
            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBarMusicPlayer.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.OnCompleted();
            showNotification(R.drawable.ic_play_24);
            fltBtnMusicPlayerPlayPause.setBackgroundResource(R.drawable.ic_play_24);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
    }

    private void metaData(Uri uri) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri.toString());
            int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
            tvMusicPlayerDurationTotal.setText(formattedTime(durationTotal));
            byte[] musicPlayerPhoto = retriever.getEmbeddedPicture();
            if (musicPlayerPhoto != null) {
                Glide.with(this).asBitmap().load(musicPlayerPhoto).into(ivMusicPlayerPhoto);
            } else {
                Glide.with(this).asBitmap().load(R.drawable.ic_launcher_background).into(ivMusicPlayerPhoto);
            }
        }catch (Exception e) {
            Log.e("Loi: ", e.getMessage());
        }
    }
    private String formattedTime(int mCurrentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }
    private void getIntentMethod() throws IOException {
        position = getIntent().getIntExtra("position", -1);
        listSongs = songList;
        if (!listSongs.isEmpty()) {
            fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        showNotification(R.drawable.ic_pause_24);
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("servicePosition", position);
        startService(intent);

    }
    private void initializeViewComponent() {
        ivMusicPlayerCloseDown = findViewById(R.id.iv_music_player_close_down);
        ivMusicPlayerOption = findViewById(R.id.iv_music_player_option);
        ivMusicPlayerPhoto = findViewById(R.id.iv_music_player_photo);
        tvMusicPlayerTitle = findViewById(R.id.tv_music_player_title);
        tvMusicPlayerArtist = findViewById(R.id.tv_music_player_artist);
        tvMusicPlayerDurationRun = findViewById(R.id.tv_music_player_duration_run);
        tvMusicPlayerDurationTotal = findViewById(R.id.tv_music_player_duration_total);
        seekBarMusicPlayer = findViewById(R.id.seek_bar_music_player);
        ivMusicPlayerShuffle = findViewById(R.id.iv_music_player_shuffle);
        ivMusicPlayerRepeat = findViewById(R.id.iv_music_player_repeat);
        ivMusicPlayerSkipPrevious = findViewById(R.id.iv_music_player_skip_previous);
        ivMusicPlayerSkipNext = findViewById(R.id.iv_music_player_skip_next);
        fltBtnMusicPlayerPlayPause = findViewById(R.id.flt_btn_music_player_play_pause);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        musicService.setCallBack(this);
        Toast.makeText(this, "Connected " + musicService, Toast.LENGTH_SHORT).show();
        seekBarMusicPlayer.setMax(musicService.getDuration() / 1000);
        metaData(uri);
        tvMusicPlayerTitle.setText(listSongs.get(position).getTitle());
        tvMusicPlayerArtist.setText(listSongs.get(position).getArtist());
        musicService.OnCompleted();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }
    void showNotification(int playPauseBtn) throws IOException {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE);

        byte[] picture = null;
        picture = getMusicPhoto(listSongs.get(position).getPath());
        Bitmap thumb = null;
        if(picture != null) {
            thumb = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        }
        else {
            thumb = BitmapFactory.decodeResource(getResources(), R.drawable.logo_image);
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(playPauseBtn)
                .setLargeIcon(thumb)
                .setContentTitle(listSongs.get(position).getTitle())
                .setContentText(listSongs.get(position).getArtist())
                .addAction(R.drawable.ic_skip_previous_24, "Previous", prevPending)
                .addAction(playPauseBtn, "Pause", pausePending)
                .addAction(R.drawable.ic_skip_next_24, "Next", nextPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(MediaSessionCompat.Token.fromToken(mediaSessionCompat.getSessionToken())))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }
    private byte[] getMusicPhoto(String uri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] photo = retriever.getEmbeddedPicture();
        retriever.release();
        return photo;
    }
}